package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.request.BabyURLRequest;
import com.babyurl.urlshortener.resolver.RedirectionBaseUrlResolver;
import com.babyurl.urlshortener.response.BabyURLResponse;
import com.babyurl.urlshortener.service.UrlShortenerService;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerAPITest {

    @Mock
    private UrlShortenerService urlShortenerService;

    @Mock
    private RedirectionBaseUrlResolver redirectionBaseUrlResolver;

    @Mock
    private HttpRequest<String> httpRequest;

    @Test
    void shouldGenerateBabyURLWithRequestBasePath() {
        String url = "http://originalURL";
        ShortenURLData shortenURLData = new ShortenURLData("key", url, Duration.ofDays(1));
        when(urlShortenerService.shortURL(url)).thenReturn(shortenURLData);
        when(redirectionBaseUrlResolver.resolve(httpRequest)).thenReturn("http://domain/");

        BabyURLRequest babyURLRequest = BabyURLRequest.builder().url(url).build();
        BabyURLResponse babyURLResponse = new UrlShortenerAPI(urlShortenerService, redirectionBaseUrlResolver)
                .shortURL(babyURLRequest, httpRequest);

        assertEquals("http://domain/key", babyURLResponse.getBabyURL());
    }

}