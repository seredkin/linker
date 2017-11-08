package com.seredkin.linker.service;

import com.example.jooq_dsl.tables.pojos.Link;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.example.jooq_dsl.Tables.LINK;

@Service @CacheConfig(cacheNames = "linker")
class LinkerServiceImpl implements LinkerService<Link> {

    private final DSLContext dsl;

    private final Codec codec;

    LinkerServiceImpl(@Autowired DSLContext dsl, @Autowired Codec codec) {
        this.dsl = dsl;
        this.codec = codec;
    }

    @Override
    public Link store(String fullLink) {
        try {
            return dsl.insertInto(LINK).columns(LINK.URL).values(fullLink).returning(LINK.fields()).fetchOne().into(Link.class);
        } catch (Exception e) {
            return dsl.selectFrom(LINK).where(LINK.URL.eq(fullLink)).fetchOneInto(Link.class);
        }
    }

    @Override
    @Cacheable
    public Link fetch(String shortLink) {
        return dsl.selectFrom(LINK).where(LINK.ID.eq(codec.decodeId(shortLink))).fetchOneInto(Link.class);
    }

    @Override
    public int delete(Long linkId) {
        return dsl.deleteFrom(LINK).where(LINK.ID.eq(linkId)).execute();
    }

}
