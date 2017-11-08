package com.seredkin.linker;

import com.example.jooq_dsl.tables.pojos.Link;
import com.seredkin.linker.service.Codec;
import com.seredkin.linker.service.LinkerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

import java.net.URI;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LinkerWebTests {

    @Value("${linker.prefix}")
    private String prefix;

    @LocalServerPort Integer port;

    @Autowired
    WebTestClient webClient;

    @Autowired
    LinkerService<Link> linkerService;
    @Autowired
    Codec codec;
    private final Random r = new Random();

    @Test
    public void fetchExisting() {
        final Link link = linkerService.store(rndLink());

        final String shortLink = codec.encodeId(link.getId());

        final Link fetchLink = linkerService.fetch(shortLink);
        assertThat(fetchLink.getId(), equalTo(link.getId()));
        assertThat(fetchLink.getUrl(), equalTo(link.getUrl()));

        final String fullLink = webClient.get().uri("/" + shortLink).exchange().returnResult(String.class)
                .getResponseBody().blockFirst();

        assertThat(fullLink, equalTo(link.getUrl()));



        linkerService.delete(link.getId());
    }

    @Test
    public void postNew() {


        String shortLink = postNewLink(rndLink());

        assertThat(shortLink, notNullValue());
        assertThat(shortLink.length(), greaterThan(0));


        /* Try to create a duplicate - should return the original */

        final Link fetchLink = linkerService.fetch(shortLink);


        String s = postNewLink(fetchLink.getUrl());

        assertThat(s, equalTo(shortLink));

        assertThat(fetchLink.getId(), equalTo(linkerService.fetch(shortLink).getId()));

        linkerService.delete(fetchLink.getId());

    }

    private String postNewLink(String fullLink) {
        String link = webClient.post().uri("/").syncBody(fullLink).exchange()
                .returnResult(String.class).getResponseBody().blockFirst();
        assertThat(link, startsWith(prefix));

        // strip down the prefix
        return link.replaceAll(prefix + "/", "");
    }

    private String rndLink() {
        return "http://random.domain/" + r.nextInt();
    }


}
