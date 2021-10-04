package com.babyurl.keygeneator.api;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("/")
public interface KeyGeneratorClient {

    @Get("/generate")
    String generate();
}
