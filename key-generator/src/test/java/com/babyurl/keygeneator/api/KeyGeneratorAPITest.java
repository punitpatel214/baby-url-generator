package com.babyurl.keygeneator.api;

import com.babyurl.keygeneator.repository.casandra.BaseCassandraContainerTest;
import com.datastax.oss.driver.api.core.CqlSession;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.truncate;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
@TestMethodOrder(OrderAnnotation.class)
class KeyGeneratorAPITest extends BaseCassandraContainerTest {

    private final KeyGeneratorClient keyGeneratorClient;

    @Inject
    public KeyGeneratorAPITest(KeyGeneratorClient keyGeneratorClient) {
        this.keyGeneratorClient = keyGeneratorClient;
    }

    private final List<String> keys = asList("key1", "key2", "key3", "key4");

    @BeforeAll
    static void beforeAll() {
        startCassandraContainer();
    }

    @BeforeEach
    void setUp() {
        super.dataSetup();
        cqlSession.execute(truncate("keys").build());
        keys.forEach(key -> cqlSession.execute(String.format("insert into keys(key) values ('%s')", key)));
    }

    @Test
    @Order(1)
    void shouldGenerateKey() {
        List<String> generatedKeys = IntStream.range(0, keys.size())
                .mapToObj(index -> keyGeneratorClient.generate())
                .sorted()
                .collect(Collectors.toList());
        assertEquals(keys, generatedKeys);
    }

    @Test
    void shouldGenerateNoResponseIfAllKeysUsed() {
        cqlSession.execute(truncate("keys").build());
        assertThrows(HttpClientResponseException.class, keyGeneratorClient::generate);
    }


    @MockBean(CqlSession.class)
    public CqlSession getCqlSession() {
        return cqlSession;
    }
}