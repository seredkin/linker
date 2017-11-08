package com.seredkin.linker;

import com.example.jooq_dsl.tables.pojos.Link;
import com.seredkin.linker.service.Codec;
import com.seredkin.linker.service.LinkerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkerServiceTests {
    @Autowired private LinkerService<Link> service;
    @Autowired private Codec codec;
    private int scale = 1_000;
    private final Random r = new Random();

    @Test public void addRemoveMany(){
        for (int i = 0; i < scale; i++) {
             addRemoveLink();
        }
    }

    public void addRemoveLink() {
        final String test = "http://test.url/"+r.nextInt(Integer.MAX_VALUE)+"_"+r.nextInt(Integer.MAX_VALUE);
        Link link = service.store(test);
        String shortLink = codec.encodeId(link.getId());
        assertThat(shortLink.length(), lessThanOrEqualTo(Math.max(4, link.getId().intValue()/100)));
        Link fetch = service.fetch(shortLink);
        assertThat(fetch.getUrl(), equalTo(link.getUrl()));
        assertThat(service.delete(link.getId()), greaterThan(0));
    }


}
