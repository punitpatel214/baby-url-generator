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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockServerExtension.class)
class APIIntegrationTest extends BaseCassandraContainerTest {

    private static final String KEY = "anyKey";

    private  ClientAndServer clientAndServer;

    @Inject
    private UrlShortenerAPIClient urlShortenerAPIClient;

    @Inject
    private RedisClient redisClient;

    @BeforeEach
    void setUp(ClientAndServer clientAndServer) {
        this.clientAndServer = clientAndServer;
    }

    @Test
    void shouldShortenURL() {
        String path = "/test/long/url";
        mockRedirectionURL(path);
        String url = format("http://localhost:%d%s",  clientAndServer.getPort() , path);
        generateShortURL(url)
                .verifyRedirection()
                .verifyCacheRedirection()
                .removeFromCache()
                .generateShortURL(url)
                .expiryUrlDB()
                .verifyExpireURLResponse()
                .verifyNoRedirectionOfUnknownURL()
                .verifyBadRequestForInvalidURL();
    }

    private void mockRedirectionURL(String path) {
         new MockServerClient("localhost", clientAndServer.getPort())
                .when(request().withMethod("GET")
                        .withPath(path))
                .respond(response("redirectSuccess"));
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

    private APIIntegrationTest verifyCacheRedirection() {
        deleteAllURLS();
        return verifyRedirection();
    }

    private APIIntegrationTest verifyExpireURLResponse() {
        HttpClientResponseException httpClientResponseException = assertThrows(HttpClientResponseException.class, () -> urlShortenerAPIClient.get(KEY));
        HttpResponse<?> httpResponse = httpClientResponseException.getResponse();
        assertEquals(HttpStatus.GONE, httpResponse.getStatus());
        assertEquals("URL is expired", httpResponse.getBody().orElseThrow());
        return this;
    }

    private APIIntegrationTest expiryUrlDB() {
        expireUrl(KEY);
        return this;
    }

    private APIIntegrationTest removeFromCache() {
        redisClient.connect().sync().del("urls:" + KEY);
        return this;
    }

    private APIIntegrationTest verifyNoRedirectionOfUnknownURL() {
        HttpResponse<String> httpResponse = urlShortenerAPIClient.get("notFoundKey");
        assertEquals(HttpStatus.NOT_FOUND, httpResponse.getStatus());
        return this;
    }

    private APIIntegrationTest verifyBadRequestForInvalidURL() {
        String invalidURL = "http://invalidURL^$&%$&^";
        HttpClientResponseException httpClientResponseException = assertThrows(HttpClientResponseException.class,
                () -> urlShortenerAPIClient.shortenURL(invalidURL));
        HttpResponse<?> httpResponse = httpClientResponseException.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST, httpResponse.getStatus());
        String responseBody = httpResponse.getBody().orElseThrow().toString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains(format("url: (%s) is not valid", invalidURL)));
        return this;
    }

    @MockBean(KeyGeneratorClient.class)
    public KeyGeneratorClient keyGeneratorClient() {
        return () -> KEY;
    }
}