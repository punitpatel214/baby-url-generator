package com.babyurl.keygeneator.api;

import com.babyurl.keygeneator.repository.casandra.BaseCassandraContainerTest;
import com.datastax.oss.driver.api.core.CqlSession;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class KeyGeneratorAPITest extends BaseCassandraContainerTest {

    private KeyGeneratorClient keyGeneratorClient;

    @Inject
    public KeyGeneratorAPITest(KeyGeneratorClient keyGeneratorClient) {
        this.keyGeneratorClient = keyGeneratorClient;
    }

    private final List<String> keys = asList("key1", "key2", "key3", "key4", "key5");

    @BeforeAll
    static void beforeAll() {
        startCassandraContainer();
    }

    @BeforeEach
    void setUp() {
        super.dataSetup();
        keys.forEach(key -> cqlSession.execute(String.format("insert into keys(key) values ('%s')", key)));
    }

    @Test
    void shouldGenerateKey() {
        List<String> generatedKeys = IntStream.range(0, keys.size())
                .mapToObj(index -> keyGeneratorClient.generate())
                .sorted()
                .collect(Collectors.toList());
        assertEquals(keys, generatedKeys);
    }

    @MockBean(CqlSession.class)
    public CqlSession getCqlSession() {
        return cqlSession;
    }
}