package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.resolver.RedirectionUrlResolver;
import com.babyurl.urlshortener.service.UrlShortenerService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller
public class UrlShortenerAPI {

    private final UrlShortenerService urlShortenerService;
    private final RedirectionUrlResolver redirectionUrlResolver;

    @Inject
    public UrlShortenerAPI(UrlShortenerService urlShortenerService, RedirectionUrlResolver redirectionUrlResolver) {
        this.urlShortenerService = urlShortenerService;
        this.redirectionUrlResolver = redirectionUrlResolver;
    }

    @Post("/shortenURL")
    public String shortURL(@Body String originalURL, HttpRequest<String> httpRequest) {
        ShortenURLData shortenURLData = urlShortenerService.shortenURL(originalURL);
        return redirectionUrlResolver.resolve(httpRequest) + shortenURLData.key;
    }

}
