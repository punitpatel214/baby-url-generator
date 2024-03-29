package com.babyurl.keygeneator.cache;

import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.runAsync;

@Singleton
public class KeyCache {

    private final KeyGeneratorRepository keyGeneratorRepository;
    private final ConcurrentLinkedQueue<String> concurrentLinkedQueue;
    private final int maxCacheSize;
    private final int minCacheSize;
    private final AtomicInteger cacheSize;
    private final AtomicBoolean cacheReloadInProcess;

    public KeyCache(KeyGeneratorRepository keyGeneratorRepository, @Value("${cache.maxSize:10}") int maxCacheSize,
                    @Value("${cache.minSize:10}") int minCacheSize) {
        this.keyGeneratorRepository = keyGeneratorRepository;
        this.maxCacheSize = maxCacheSize;
        this.minCacheSize = minCacheSize;
        this.concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        this.cacheSize = new AtomicInteger();
        cacheReloadInProcess = new AtomicBoolean(false);
    }

    @PostConstruct
    public void loadCache() {
        if (cacheReloadInProcess.get()) {
            return;
        }
        cacheReloadInProcess.set(true);
        List<String> keys = keyGeneratorRepository.getKeys(maxCacheSize);
        concurrentLinkedQueue.addAll(keys);
        cacheSize.set(concurrentLinkedQueue.size());
        cacheReloadInProcess.set(false);
    }

    public Optional<String> getKey() {
        ensureCacheCapacity(cacheSize.decrementAndGet());
        return ofNullable(concurrentLinkedQueue.poll());
    }

    private void ensureCacheCapacity(int cacheSize) {
        if (cacheSize < minCacheSize) {
            return;
        }
        runAsync(this::loadCache);
    }
}
