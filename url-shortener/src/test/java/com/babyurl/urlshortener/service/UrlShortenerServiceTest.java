package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.exception.ShortenURLFailException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private KeyGeneratorClient keyGeneratorClient;

    @Mock
    private ShortenURLRepository urlRepository;

    @Captor
    private ArgumentCaptor<ShortenURLData> urlDataArgumentCaptor;


    @Test
    void shouldGenerateShortenURL() {
        when(keyGeneratorClient.generateKey()).thenReturn("key");
        when(urlRepository.save(any(ShortenURLData.class))).thenReturn(true);
        LocalDateTime testStartTime = LocalDateTime.now();

        UrlShortenerService urlShortenerService = new UrlShortenerService(keyGeneratorClient, urlRepository, Duration.ofMinutes(10));
        ShortenURLData shortURL = urlShortenerService.shortURL("originalURL");

        verify(urlRepository, times(1)).save(urlDataArgumentCaptor.capture());
        assertEquals("key", shortURL.key);
        assertEquals("originalURL", shortURL.originalURL);
        Duration duration = Duration.between(shortURL.createTime, testStartTime);
        assertTrue(duration.getSeconds() < 5);
        assertEquals(shortURL.expiryTime, shortURL.createTime.plusMinutes(10));
    }

    @Test
    void shouldThrowExceptionWhenUrlSaveFail() {
        when(keyGeneratorClient.generateKey()).thenReturn("k1");
        when(urlRepository.save(any(ShortenURLData.class))).thenReturn(false);

        UrlShortenerService urlShortenerService = new UrlShortenerService(keyGeneratorClient, urlRepository, Duration.ofMinutes(10));
        assertThrows(ShortenURLFailException.class, () -> urlShortenerService.shortURL("anyURL"), "DB insertion fail for key k1");
    }
}