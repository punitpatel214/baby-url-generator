package com.babyurl.keygeneator.api;

import com.babyurl.keygeneator.service.KeyGeneratorService;
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
