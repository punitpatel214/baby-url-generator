package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.exception.ShortenURLFailException;
import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.Duration;

@Singleton
public class UrlShortenerService {

    private final KeyGeneratorClient keyGeneratorClient;
    private final ShortenURLRepository urlRepository;
    private final Duration expiryDuration;

    @Inject
    // TODO check how can we implement build in time suffix s for seconds
    public UrlShortenerService(KeyGeneratorClient keyGeneratorClient, ShortenURLRepository urlRepository,
                               @Value("${baby-url.expiry.duration}") Duration expiryDuration) {
        this.keyGeneratorClient = keyGeneratorClient;
        this.urlRepository = urlRepository;
        this.expiryDuration = expiryDuration;
    }

    public ShortenURLData shortURL(String originalURL) {
        String key = keyGeneratorClient.generateKey();
        ShortenURLData urlData = new ShortenURLData(key, originalURL, expiryDuration);
        boolean save = urlRepository.save(urlData);
        if (!save) {
            throw new ShortenURLFailException("DB insertion fail for key " + urlData.key);
        }
        return urlData;
    }
}
