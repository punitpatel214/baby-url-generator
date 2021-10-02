package com.babyurl.api;

import com.babyurl.repository.KeyGeneratorRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/generate")
public class KeyGeneratorAPI {

    private final KeyGeneratorRepository keyGeneratorRepository;

    private int sequence = 0;

    public KeyGeneratorAPI(KeyGeneratorRepository keyGeneratorRepository) {
        this.keyGeneratorRepository = keyGeneratorRepository;
    }

    @Get
    public void generateKeys() {
        keyGeneratorRepository.insertKey("abc" + sequence++);
    }

}
