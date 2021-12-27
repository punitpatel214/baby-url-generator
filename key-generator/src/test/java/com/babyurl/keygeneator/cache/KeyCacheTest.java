package com.babyurl.keygeneator.cache;

import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
class KeyCacheTest {

    @Mock
    private KeyGeneratorRepository keyGeneratorRepository;

    private KeyCache keyCache;

    @BeforeEach
    void setUp() {
        when(keyGeneratorRepository.getKeys(4)).thenReturn(asList("Key1", "Key2", "Key3", "Key4"));
        keyCache = new KeyCache(keyGeneratorRepository, 4, 2);
        keyCache.loadCache();
    }

    @Test
    void shouldGetKeyFromCache() {
        Optional<String> key = keyCache.getKey();
        assertTrue(key.isPresent());
    }

    @Test
    void shouldGetKeyFromReloadCache() throws InterruptedException {
        IntStream.range(0, 4).forEach(index -> keyCache.getKey());
        Thread.sleep(100);
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> verify(keyGeneratorRepository, atLeast(2)).getKeys(4));
    }
}