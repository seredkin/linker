package com.seredkin.linker.service;

public interface LinkerService<L> {
    L store(String fullLink);

    L fetch(String shortLink);

    int delete(Long linkId);
}
