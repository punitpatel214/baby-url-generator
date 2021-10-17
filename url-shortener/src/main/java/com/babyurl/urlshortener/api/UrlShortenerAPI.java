package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.resolver.RedirectionUrlResolver;
import com.babyurl.urlshortener.service.UrlShortenerService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.validation.constraints.NotBlank;

@Controller
public class UrlShortenerAPI {

    private final UrlShortenerService urlShortenerService;
    private final RedirectionUrlResolver redirectionUrlResolver;

    @Inject
    public UrlShortenerAPI(UrlShortenerService urlShortenerService, RedirectionUrlResolver redirectionUrlResolver) {
        this.urlShortenerService = urlShortenerService;
        this.redirectionUrlResolver = redirectionUrlResolver;
    }

    @Post(value = "/shortenURL", consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN)
    public String shortURL(@Body @NotBlank String originalURL, HttpRequest<String> httpRequest) {
        ShortenURLData shortenURLData = urlShortenerService.shortURL(originalURL);
        return redirectionUrlResolver.resolve(httpRequest).concat(shortenURLData.key);
    }

}
