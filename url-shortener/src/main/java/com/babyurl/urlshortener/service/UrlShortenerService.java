package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.exception.ShortenURLFailException;
import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import io.micronaut.jackson.ObjectMapperFactory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static java.time.Duration.ofMinutes;

@Singleton
public class UrlShortenerService {

    private final KeyGeneratorClient keyGeneratorClient;
    private final ShortenURLRepository urlRepository;
    private final long expiryDurationInMinutes;

    @Inject
    // TODO check how can we implement build in time suffix s for seconds
    public UrlShortenerService(KeyGeneratorClient keyGeneratorClient, ShortenURLRepository urlRepository,
                               @Value("${expiry.duration.inSeconds:1440}") long expiryDurationInMinutes) {
        this.keyGeneratorClient = keyGeneratorClient;
        this.urlRepository = urlRepository;
        this.expiryDurationInMinutes = expiryDurationInMinutes;
    }

    public ShortenURLData shortURL(String originalURL) {
        String key = keyGeneratorClient.generateKey();
        ShortenURLData urlData = new ShortenURLData(key, originalURL, ofMinutes(expiryDurationInMinutes));
        boolean save = urlRepository.save(urlData);
        if (!save) {
            throw new ShortenURLFailException("DB insertion fail for key " + urlData.key);
        }
        return urlData;
    }
}
