package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.repositiry.cassandra.BaseCassandraContainerTest;
import com.babyurl.urlshortener.request.BabyURLRequest;
import com.babyurl.urlshortener.response.BabyURLResponse;
import com.google.common.collect.ImmutableMap;
import io.lettuce.core.RedisClient;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import redis.embedded.RedisServerBuilder;

import java.util.Map;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockServerExtension.class)
class APIIntegrationTest extends BaseCassandraContainerTest {

    private static final String KEY = "anyKey";

    @Inject
    private UrlShortenerAPIClient urlShortenerAPIClient;

    @Inject
    private RedisClient redisClient;

    @Test
    void shouldShortenURL(ClientAndServer clientAndServer) {
        String path = "/test/long/url";
        mockRedirectionURL(path, clientAndServer);
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

    private void mockRedirectionURL(String path, ClientAndServer clientAndServer) {
        clientAndServer.when(request().
                withMethod("GET")
                .withPath(path))
                .respond(response("redirectSuccess"));
    }

    private APIIntegrationTest generateShortURL(String url) {
        BabyURLResponse babyURLResponse = urlShortenerAPIClient.shortenURL(babyURLRequestBody(url));
        String babyURL = babyURLResponse.getBabyURL();
        assertTrue(babyURL.endsWith(KEY));
        return this;
    }

    private BabyURLRequest babyURLRequestBody(String url) {
        return BabyURLRequest.builder().url(url).build();
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
                () -> urlShortenerAPIClient.shortenURLInvalidRequest(babyURLRequestBody(invalidURL)));
        HttpResponse<?> httpResponse = httpClientResponseException.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST, httpResponse.getStatus());
        String responseBody = httpResponse.getBody().orElseThrow().toString();
        assertTrue(responseBody.contains(format("url: (%s) is not valid", invalidURL)));
        return this;
    }

    @MockBean(KeyGeneratorClient.class)
    public KeyGeneratorClient keyGeneratorClient() {
        return () -> KEY;
    }

    @Override
    @NonNull
    public Map getProperties() {
        int redisPort = SocketUtils.findAvailableTcpPort();
        startRedis(redisPort);
        return ImmutableMap.builder()
                .putAll(super.getProperties())
                .put("redis.uri", "redis://localhost:" + redisPort)
                .build();
    }

    private void startRedis(int availableTcpPort) {
        new RedisServerBuilder()
                .port(availableTcpPort)
                .build();
    }
}