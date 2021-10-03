package com.babyurl.api;

import com.babyurl.service.KeyGeneratorService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/generate")
public class KeyGeneratorAPI {

    private final KeyGeneratorService keyGeneratorService;

    public KeyGeneratorAPI(KeyGeneratorService keyGeneratorService) {
        this.keyGeneratorService = keyGeneratorService;
    }

    @Get
    public String generateKeys() {
        return keyGeneratorService.generateKey();
    }

}
