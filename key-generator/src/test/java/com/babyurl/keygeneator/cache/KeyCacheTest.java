package com.babyurl.keygeneator.cache;

import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyCacheTest {

    @Mock
    private KeyGeneratorRepository keyGeneratorRepository;

    private KeyCache keyCache;

    @BeforeEach
    void setUp() {
        when(keyGeneratorRepository.getKeys(4)).thenReturn(asList("Key1", "Key2", "Key3", "Key4"));
        keyCache = new KeyCache(keyGeneratorRepository, 4);
        keyCache.init();
    }

    @Test
    void shouldGetKeyFromCache() {
        Optional<String> key = keyCache.getKey();
        assertTrue(key.isPresent());
    }

    @Test
    void shouldGetKeyFromReloadCache() {
        IntStream.range(0, 4).forEach(index -> keyCache.getKey());
        verify(keyGeneratorRepository, times(2)).getKeys(4);
    }
}