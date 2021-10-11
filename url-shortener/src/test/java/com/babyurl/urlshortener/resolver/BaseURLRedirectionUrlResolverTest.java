package com.babyurl.urlshortener.resolver;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.HttpHostResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseURLRedirectionUrlResolverTest {

    @Mock
    private HttpHostResolver httpHostResolver;

    @Mock
    private HttpRequest<Object> httpRequest;

    @Test
    void shouldCreateBaseURLAsRedirectionURL() {
        when(httpHostResolver.resolve(httpRequest)).thenReturn("http://base_url");

        BaseURLRedirectionUrlResolver redirectionUrlResolver = new BaseURLRedirectionUrlResolver(httpHostResolver, "");
        String resolve = redirectionUrlResolver.resolve(httpRequest);

        assertEquals("http://base_url/", resolve);
    }


    @Test
    void shouldCreateBaseURLAsRedirectionURLWithContextPath() {
        when(httpHostResolver.resolve(httpRequest)).thenReturn("http://base_url");

        BaseURLRedirectionUrlResolver redirectionUrlResolver = new BaseURLRedirectionUrlResolver(httpHostResolver, "/contextPath");
        String resolve = redirectionUrlResolver.resolve(httpRequest);

        assertEquals("http://base_url/contextPath/", resolve);
    }
}