package com.babyurl.keygeneator.service;

import com.babyurl.keygeneator.cache.KeyCache;
import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import jakarta.inject.Singleton;

@Singleton
public class KeyGeneratorService {
    private final KeyCache keyCache;
    private final KeyGeneratorRepository keyGeneratorRepository;

    public KeyGeneratorService(KeyCache keyCache, KeyGeneratorRepository keyGeneratorRepository) {
        this.keyCache = keyCache;
        this.keyGeneratorRepository = keyGeneratorRepository;
    }

    public String generateKey() {
        return keyCache.getKey()
                .orElseGet(keyGeneratorRepository::getKey);
    }

}
