package com.seredkin.linker;

import com.example.jooq_dsl.Tables;
import com.example.jooq_dsl.tables.pojos.Link;
import com.example.jooq_dsl.tables.records.LinkRecord;
import org.hamcrest.Matchers;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.jooq_dsl.Tables.LINK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkerDBTests {
    private @Autowired DSLContext dsl;

    @Test
    public void addRemoveLink() {
        final String test = "http://test.url";

        dsl.transaction(configuration -> {
            Link newLink = DSL.using(configuration).insertInto(LINK).columns(LINK.URL).values(test).returning(LINK.fields()).fetchOne().into(Link.class);
            assertThat(newLink.getId(), notNullValue());
            assertThat(newLink.getCreationDate(), notNullValue());

            int del = DSL.using(configuration).deleteFrom(LINK).where(LINK.ID.eq(newLink.getId())).execute();

            assertThat(del, equalTo(1));

        });
    }


}
