package com.babyurl.urlshortener.resolver;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.HttpHostResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextPathRedirectionBaseUrlResolverTest {

    @Mock
    private HttpHostResolver httpHostResolver;

    @Mock
    private HttpRequest<String> httpRequest;

    @Test
    void shouldCreateBaseURLAsRedirectionURL() {
        when(httpHostResolver.resolve((eq(httpRequest)))).thenReturn("http://base_url");

        ContextPathRedirectionBaseUrlResolver redirectionBaseUrlResolver = new ContextPathRedirectionBaseUrlResolver(httpHostResolver, "");
        String resolve = redirectionBaseUrlResolver.resolve(httpRequest);

        assertEquals("http://base_url/", resolve);
    }


    @Test
    void shouldCreateBaseURLAsRedirectionURLWithContextPath() {
        when(httpHostResolver.resolve(eq(httpRequest))).thenReturn("http://base_url");

        ContextPathRedirectionBaseUrlResolver redirectionBaseUrlResolver = new ContextPathRedirectionBaseUrlResolver(httpHostResolver, "/contextPath");
        String resolve = redirectionBaseUrlResolver.resolve(httpRequest);

        assertEquals("http://base_url/contextPath/", resolve);
    }
}