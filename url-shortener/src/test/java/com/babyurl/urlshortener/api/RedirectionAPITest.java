package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.service.RedirectionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedirectionAPITest {

    @Mock
    private RedirectionService redirectionService;

    @Test
    void shouldRedirectToURL() {
        Redirection redirection = new Redirection("key", "redirectionURL", now().plusHours(1));
        when(redirectionService.findRedirection("key")).thenReturn(of(redirection));

        RedirectionAPI redirectionAPI = new RedirectionAPI(redirectionService);
        HttpResponse<Object> httpResponse = redirectionAPI.redirect(redirection.getKey());

        assertEquals(HttpStatus.MOVED_PERMANENTLY, httpResponse.getStatus());
        assertEquals(redirection.getUrl(), httpResponse.getHeaders().get("location"));
    }

    @Test
    void shouldReturnHttpStatusNotFoundWhenURlNotExists() {
        when(redirectionService.findRedirection("key")).thenReturn(empty());

        RedirectionAPI redirectionAPI = new RedirectionAPI(redirectionService);
        HttpResponse<Object> httpResponse = redirectionAPI.redirect("key");

        assertEquals(HttpStatus.NOT_FOUND, httpResponse.getStatus());
    }
}