package com.seredkin.linker.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component @Qualifier("Base64")
class CodecBase64Impl implements Codec {
    @Override
    public Long decodeId(String base64id) {
        final int upTo4 = 4 - base64id.length() % 4;
        if (upTo4 > 0 && upTo4 < 4) {
            for (int i = 0; i < upTo4; i++) {
                base64id = base64id.concat("=");
            }
        }

        return Long.valueOf(new String(Base64.getDecoder().decode(base64id)));
    }

    @Override
    public String encodeId(Long plainId) {
        return Base64.getEncoder().encodeToString(plainId.toString().getBytes()).replaceAll("=", "");
    }
}
