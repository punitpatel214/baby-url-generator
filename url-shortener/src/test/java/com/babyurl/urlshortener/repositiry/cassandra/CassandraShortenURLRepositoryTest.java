package com.babyurl.urlshortener.repositiry.cassandra;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.model.ShortenURLData;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.util.Optional;

import static com.babyurl.urlshortener.repositiry.cassandra.ShortenURLTableMetaData.tableName;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CassandraShortenURLRepositoryTest extends BaseCassandraContainerTest {

    @Inject
    private CqlSession cqlSession;

    @BeforeEach
    void setUp() {
        cqlSession.execute(QueryBuilder.truncate(tableName).build());
    }

    @Test
    void shouldSaveUrl() {
        CassandraShortenURLRepository repository = new CassandraShortenURLRepository(cqlSession);
        ShortenURLData urlData = new ShortenURLData("key",  "http://origninalurl", Duration.ofHours(1));
        boolean isInserted = repository.save(urlData);
        assertTrue(isInserted);
    }

    @Test
    void shouldNotSaveUrlWhenKeyAlreadyExits() {
        CassandraShortenURLRepository repository = new CassandraShortenURLRepository(cqlSession);
        ShortenURLData urlData = new ShortenURLData("key",  "http://origninalurl", Duration.ofHours(1));
        repository.save(urlData);

        assertFalse(repository.save(urlData));
    }

    @Test
    void shouldGetRedirectionData() {
        // given
        CassandraShortenURLRepository repository = new CassandraShortenURLRepository(cqlSession);
        ShortenURLData urlData = new ShortenURLData("key",  "http://originalurl", Duration.ofHours(1));
        repository.save(urlData);

        //when
        Redirection redirection = repository.find("key").orElseThrow();
        assertEquals("http://originalurl", redirection.getUrl());
        assertEquals("key", redirection.getKey());
        assertNotNull(redirection.getExpiryTime());
    }

    @Test
    void shouldNotGetRedirectionData() {
        CassandraShortenURLRepository repository = new CassandraShortenURLRepository(cqlSession);
        Optional<Redirection> redirection = repository.find("key");
        assertTrue(redirection.isEmpty());
    }
}