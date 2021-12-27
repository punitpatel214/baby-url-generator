package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.request.BabyURLRequest;
import com.babyurl.urlshortener.response.BabyURLResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client(value = "/")
public interface UrlShortenerAPIClient {

    @Post(value = "/shortenURL")
    BabyURLResponse shortenURL(@Body BabyURLRequest babyURLRequest);

    @Get("/{key}")
    HttpResponse<String> get(String key);

    @Post(value = "/shortenURL")
    HttpResponse<String> shortenURLInvalidRequest(@Body BabyURLRequest babyURLRequest);

}
