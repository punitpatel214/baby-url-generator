package com.babyurl.urlshortener.resolver;

import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RedirectionUrlConfigResolverTest {

    @Test
    void shouldReturnConfigureRedirectionURL() {
        RedirectionUrlConfigResolver redirectionUrlConfigResolver = new RedirectionUrlConfigResolver("redirectionURL");
        String resolveURL = redirectionUrlConfigResolver.resolve(Mockito.any());
        assertEquals("redirectionURL", resolveURL);
    }
}