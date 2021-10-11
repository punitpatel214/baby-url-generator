package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.client.KeyGeneratorClient;
import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static java.time.Duration.ofMinutes;

@Singleton
public class UrlShortenerService {

    private final KeyGeneratorClient keyGeneratorClient;
    private final ShortenURLRepository urLrepository;
    private final long expiryDurationInMinutes;

    @Inject
    public UrlShortenerService(KeyGeneratorClient keyGeneratorClient, ShortenURLRepository urLrepository,
                               @Value("${expiry.duration.inMinutes:1440}") long expiryDurationInMinutes) {
        this.keyGeneratorClient = keyGeneratorClient;
        this.urLrepository = urLrepository;
        this.expiryDurationInMinutes = expiryDurationInMinutes;
    }

    public ShortenURLData shortenURL(String originalURL) {
        String key = keyGeneratorClient.generateKey();
        ShortenURLData urlData = new ShortenURLData(key, originalURL, ofMinutes(expiryDurationInMinutes));
        urLrepository.save(urlData);
        return urlData;
    }
}
