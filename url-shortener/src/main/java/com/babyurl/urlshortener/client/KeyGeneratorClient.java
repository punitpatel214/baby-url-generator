package com.babyurl.urlshortener.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;

@Client("${keygenerator.api.url}")
@Retryable
public interface KeyGeneratorClient {

    @Get("/generate")
    String generateKey();
}
