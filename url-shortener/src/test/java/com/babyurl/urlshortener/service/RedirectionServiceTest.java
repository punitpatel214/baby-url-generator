package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedirectionServiceTest {

    @Mock
    private ShortenURLRepository shortenURLRepository;

    @Test
    void shouldFindRedirectionByKey() {
        Redirection redirection = new Redirection("key", "redirectionURL", LocalDateTime.MAX);
        Optional<Redirection> redirectionOptional = Optional.of(redirection);
        when(shortenURLRepository.find(eq("key"))).thenReturn(redirectionOptional);

        RedirectionService redirectionService = new RedirectionService(shortenURLRepository);
        Optional<Redirection> actualRedirection = redirectionService.findRedirection(redirection.getKey());

        assertEquals(redirectionOptional, actualRedirection);
    }
}