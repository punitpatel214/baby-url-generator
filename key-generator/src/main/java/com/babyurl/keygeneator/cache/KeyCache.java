package com.babyurl.keygeneator.cache;

import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class KeyCache {

    private final KeyGeneratorRepository keyGeneratorRepository;
    private final ConcurrentLinkedQueue<String> concurrentLinkedQueue;
    private final int maxCacheSize;
    private final AtomicInteger cacheSize;
    private boolean cacheReloadInProcess = false;

    public KeyCache(KeyGeneratorRepository keyGeneratorRepository,  @Value("${maxCacheSize:10}") int maxCacheSize) {
        this.keyGeneratorRepository = keyGeneratorRepository;
        this.maxCacheSize = maxCacheSize;
        this.concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        this.cacheSize = new AtomicInteger();
    }

    @PostConstruct
    public void init() {
        if (cacheReloadInProcess) {
            return;
        }
        cacheReloadInProcess = true;
        List<String> keys = keyGeneratorRepository.getKeys(maxCacheSize);
        concurrentLinkedQueue.addAll(keys);
        int size = concurrentLinkedQueue.size();
        cacheSize.set(size);
        cacheReloadInProcess = false;
    }

    public Optional<String> getKey() {
        ensureCacheCapacity(cacheSize.decrementAndGet());
        return Optional.ofNullable(concurrentLinkedQueue.poll());
    }

    private void ensureCacheCapacity(int cacheSize) {
        if (cacheSize > (maxCacheSize * 0.5)) {
            return;
        }
        CompletableFuture.runAsync(this::init);
    }
}
