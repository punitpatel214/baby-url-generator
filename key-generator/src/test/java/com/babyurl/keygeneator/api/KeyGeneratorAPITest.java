package com.babyurl.keygeneator.api;

import com.babyurl.keygeneator.Application;
import com.babyurl.keygeneator.repository.casandra.BaseCassandraContainerTest;
import com.datastax.oss.driver.api.core.CqlSession;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
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

@MicronautTest(application = Application.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyGeneratorAPITest extends BaseCassandraContainerTest implements TestPropertyProvider {

    @Inject
    private KeyGeneratorClient keyGeneratorClient;

    @Inject
    private CqlSession cqlSession;

    private final List<String> keys = asList("key1", "key2", "key3", "key4");

    @BeforeEach
    void setUp() {
        super.dataSetup(cqlSession);
        cqlSession.execute(truncate("keys").build());
    }

    @Test
    @Order(1)
    void shouldGenerateKey() {
        keys.forEach(key -> cqlSession.execute(String.format("insert into keys(key) values ('%s')", key)));
        List<String> generatedKeys = IntStream.range(0, keys.size())
                .mapToObj(index -> keyGeneratorClient.generate())
                .sorted()
                .collect(Collectors.toList());
        assertEquals(keys, generatedKeys);
    }

    @Test
    void shouldGenerateNoResponseIfAllKeysUsed() {
        assertThrows(HttpClientResponseException.class, keyGeneratorClient::generate);
    }

}