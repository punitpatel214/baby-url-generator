package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.repositiry.cassandra.BaseCassandraContainerTest;
import io.lettuce.core.RedisClient;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class APIIntegrationTest extends BaseCassandraContainerTest {

    private static final String KEY = "anyKey";

    @Inject
    private UrlShortenerAPIClient urlShortenerAPIClient;

    @Inject
    private RedisClient redisClient;

    @Test
    void shouldShortenURL() {
        String url = "/test/long/url";
        generateShortURL(url)
                .verifyRedirection()
                .verifyExpireURLResponse()
                .verifyNoRedirection();
    }

    private APIIntegrationTest generateShortURL(String url) {
        String shortenURL = urlShortenerAPIClient.shortenURL(url);
        assertTrue(shortenURL.endsWith(KEY));
        return this;
    }

    private APIIntegrationTest verifyRedirection() {
        HttpResponse<String> httpResponse = urlShortenerAPIClient.get(KEY);
        assertEquals(HttpStatus.OK, httpResponse.getStatus());
        assertEquals("redirectSuccess", httpResponse.getBody().orElseThrow());
        return this;
    }

    private APIIntegrationTest verifyExpireURLResponse() {
        expireURL(KEY);
        redisClient.connect().sync().del("urls:" + KEY);
        HttpClientResponseException httpClientResponseException = assertThrows(HttpClientResponseException.class, () -> urlShortenerAPIClient.get(KEY));
        HttpResponse<?> httpResponse = httpClientResponseException.getResponse();
        assertEquals(HttpStatus.GONE, httpResponse.getStatus());
        assertEquals("URL is expired", httpResponse.getBody().orElseThrow());
        return this;
    }



    private void verifyNoRedirection() {
        HttpResponse<String> httpResponse = urlShortenerAPIClient.get("notFoundKey");
        assertEquals(HttpStatus.NOT_FOUND, httpResponse.getStatus());
    }

    @MockBean(KeyGeneratorClient.class)
    public KeyGeneratorClient keyGeneratorClient() {
        return () -> KEY;
    }
}