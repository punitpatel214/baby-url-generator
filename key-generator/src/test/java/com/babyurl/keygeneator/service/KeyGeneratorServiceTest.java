package com.babyurl.keygeneator.service;

import com.babyurl.keygeneator.cache.KeyCache;
import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyGeneratorServiceTest {

    @Mock
    private KeyCache keyCache;

    @Mock
    private KeyGeneratorRepository keyGeneratorRepository;

    @InjectMocks
    private KeyGeneratorService keyGeneratorService;

    @Test
    void shouldGetKeyFromCache() {
        when(keyCache.getKey()).thenReturn(Optional.of("fromCache"));

        String key = keyGeneratorService.generateKey();
        assertEquals("fromCache", key);
        verify(keyGeneratorRepository, never()).getKey();
    }

    @Test
    void shouldGetKeyFromRepositoryWhenCacheReturnNoKey() {
        when(keyCache.getKey()).thenReturn(Optional.empty());
        when(keyGeneratorRepository.getKey()).thenReturn("fromRepository");

        String key = keyGeneratorService.generateKey();
        assertEquals("fromRepository", key);
    }
}