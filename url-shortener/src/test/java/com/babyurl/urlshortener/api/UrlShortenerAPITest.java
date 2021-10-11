package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.resolver.RedirectionUrlResolver;
import com.babyurl.urlshortener.service.UrlShortenerService;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerAPITest {

    @Mock
    private UrlShortenerService urlShortenerService;

    @Mock
    private RedirectionUrlResolver redirectionUrlResolver;

    @Mock
    private HttpRequest<String> httpRequest;

    @Test
    void shouldGenerateBabyURLWithRequestBasePath() {
        String originalURL = "http://originalURL";
        ShortenURLData shortenURLData = new ShortenURLData("key", originalURL, Duration.ofDays(1));
        when(urlShortenerService.shortenURL(originalURL)).thenReturn(shortenURLData);
        when(redirectionUrlResolver.resolve(httpRequest)).thenReturn("http://domain/");

        String redirectionURL = new UrlShortenerAPI(urlShortenerService, redirectionUrlResolver)
                .shortURL(originalURL, httpRequest);

        assertEquals("http://domain/key", redirectionURL);
    }

}