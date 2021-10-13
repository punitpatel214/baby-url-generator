package com.babyurl.urlshortener.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client("/")
public interface UrlShortenerAPIClient {

    @Post(value = "/shortenURL", consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    String shortenURL(@Body String url);

    @Get("/{key}")
    HttpResponse<String> get(String key);
}
