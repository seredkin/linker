package com.seredkin.linker.service;

public interface Codec {
    Long decodeId(String base64id);

    String encodeId(Long plainId);
}
