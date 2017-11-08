package com.seredkin.linker;

import com.example.jooq_dsl.tables.pojos.Link;
import com.seredkin.linker.service.Codec;
import com.seredkin.linker.service.LinkerService;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SpringBootApplication
@EnableCaching
@RestController
public class LinkerApplication {

    @Value("${linker.prefix}")
    private String prefix;
    @Autowired
    private LinkerService<Link> linkerService;
    @Autowired
    private Codec codec;

    public static void main(String[] args) {SpringApplication.run(LinkerApplication.class, args);}

    @RequestMapping(path = "/", method = POST)
    String postNew(@RequestBody String link) {
        return prefix + "/" + codec.encodeId(linkerService.store(link).getId());
    }

    @RequestMapping(path = "/{id}", method = GET)
    String getFullLink(@PathVariable String id) {
        return linkerService.fetch(id).getUrl();
    }

    @Bean
    public DSLContext dsl(@Autowired DataSource ds) {
        return new DefaultDSLContext(ds, SQLDialect.POSTGRES);
    }
}
