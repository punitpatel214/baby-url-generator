package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.request.BabyURLRequest;
import com.babyurl.urlshortener.resolver.RedirectionUrlResolver;
import com.babyurl.urlshortener.response.BabyURLResponse;
import com.babyurl.urlshortener.service.UrlShortenerService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import javax.validation.Valid;

@Controller
public class UrlShortenerAPI {

    private final UrlShortenerService urlShortenerService;
    private final RedirectionUrlResolver redirectionUrlResolver;

    @Inject
    public UrlShortenerAPI(UrlShortenerService urlShortenerService, RedirectionUrlResolver redirectionUrlResolver) {
        this.urlShortenerService = urlShortenerService;
        this.redirectionUrlResolver = redirectionUrlResolver;
    }

    @Post(value = "/shortenURL")
    public BabyURLResponse shortURL(@Body @Valid BabyURLRequest babyURLRequest, HttpRequest<String> httpRequest) {
        ShortenURLData shortenURLData = urlShortenerService.shortURL(babyURLRequest.getUrl());
        String babyURL = redirectionUrlResolver.resolve(httpRequest).concat(shortenURLData.key);
        return BabyURLResponse.builder()
                .babyURL(babyURL)
                .url(babyURLRequest.getUrl())
                .build();
    }

}
