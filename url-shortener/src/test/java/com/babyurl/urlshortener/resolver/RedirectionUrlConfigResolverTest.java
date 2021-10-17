package com.babyurl.urlshortener.resolver;

import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RedirectionUrlConfigResolverTest {

    @Mock
    private HttpRequest<String> httpRequest;

    @Test
    void shouldReturnConfigureRedirectionURL() {
        RedirectionUrlConfigResolver redirectionUrlConfigResolver = new RedirectionUrlConfigResolver("redirectionURL");
        String resolveURL = redirectionUrlConfigResolver.resolve(httpRequest);
        assertEquals("redirectionURL", resolveURL);
        verifyNoInteractions(httpRequest);
    }
}