package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.ofDays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private KeyGeneratorClient keyGeneratorClient;

    @Mock
    private ShortenURLRepository urLrepository;

    @Captor
    private ArgumentCaptor<ShortenURLData> urlDataArgumentCaptor;

    @Test
    void shouldGenerateShortenURL() {
        when(keyGeneratorClient.generateKey()).thenReturn("key");

        Duration expiryTime = ofDays(1);
        LocalDateTime testStartTime = LocalDateTime.now();
        ShortenURLData shortURL = new UrlShortenerService(keyGeneratorClient, urLrepository, expiryTime.toMinutes())
                .shortenURL("originalURL");

        verify(urLrepository, times(1)).save(urlDataArgumentCaptor.capture());
        assertEquals("key", shortURL.key);

        assertEquals("originalURL", shortURL.originalURL);
        Duration duration = Duration.between(shortURL.createTime, testStartTime);
        assertTrue(duration.getSeconds() < 5);
        assertEquals(shortURL.createTime.plus(expiryTime), shortURL.expiryTime);
    }
}