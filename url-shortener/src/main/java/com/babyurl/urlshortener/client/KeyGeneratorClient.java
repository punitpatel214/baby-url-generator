package com.babyurl.urlshortener.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("${keygenerator.api.url}")
public interface KeyGeneratorClient {

    @Get("/generate")
    String generateKey();
}
