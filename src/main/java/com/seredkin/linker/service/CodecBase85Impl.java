package com.seredkin.linker.service;

import com.github.fzakaria.ascii85.Ascii85;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
class CodecBase85Impl implements Codec {
    @Override
    public Long decodeId(String base64id) {
        return Long.parseLong(new String(Ascii85.decode(base64id), StandardCharsets.US_ASCII));
    }

    @Override
    public String encodeId(Long plainId) {
        return Ascii85.encode(plainId.toString().getBytes());
    }
}
