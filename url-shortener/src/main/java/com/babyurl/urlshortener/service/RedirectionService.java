package com.babyurl.urlshortener.service;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
@CacheConfig(cacheNames = "urls")
public class RedirectionService {
    private final ShortenURLRepository shortenURLRepository;

    @Inject
    public RedirectionService(ShortenURLRepository shortenURLRepository) {
        this.shortenURLRepository = shortenURLRepository;
    }

    @Cacheable
    public Optional<Redirection> findRedirection(String key) {
        return shortenURLRepository.find(key);
    }
}
