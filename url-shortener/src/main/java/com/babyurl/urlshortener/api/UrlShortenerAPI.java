package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.request.BabyURLRequest;
import com.babyurl.urlshortener.resolver.RedirectionBaseUrlResolver;
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
    private final RedirectionBaseUrlResolver redirectionBaseUrlResolver;

    @Inject
    public UrlShortenerAPI(UrlShortenerService urlShortenerService, RedirectionBaseUrlResolver redirectionBaseUrlResolver) {
        this.urlShortenerService = urlShortenerService;
        this.redirectionBaseUrlResolver = redirectionBaseUrlResolver;
    }

    @Post(value = "/shortenURL")
    public BabyURLResponse shortURL(@Body @Valid BabyURLRequest babyURLRequest, HttpRequest<String> httpRequest) {
        String redirectionBaseURL = redirectionBaseUrlResolver.resolve(httpRequest);
        ShortenURLData shortenURLData = urlShortenerService.shortURL(babyURLRequest.getUrl());
        return BabyURLResponse.builder()
                .babyURL(shortenURLData.babyURL(redirectionBaseURL))
                .url(babyURLRequest.getUrl())
                .build();
    }

}
