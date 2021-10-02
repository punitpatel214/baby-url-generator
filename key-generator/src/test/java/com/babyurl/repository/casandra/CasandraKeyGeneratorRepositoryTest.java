package com.babyurl.repository.casandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetSocketAddress;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CasandraKeyGeneratorRepositoryTest {

    public static CassandraContainer<?> cassandra = new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
            .withExposedPorts(9042)
            .withStartupTimeout(Duration.ofMinutes(3));

    @BeforeAll
    static void beforeAll() {
        cassandra.start();
    }

    private CqlSession cqlSession;

    private static final String KEY_VALUE = "randomKey";

    @BeforeEach
    void setUp() {
        if (cqlSession == null) {
            cqlSession = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(cassandra.getContainerIpAddress(), cassandra.getMappedPort(9042)))
                    .withLocalDatacenter("datacenter1")
                    .build();
            CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace("baby_url")
                    .ifNotExists()
                    .withSimpleStrategy(1);

            cqlSession.execute(createKeyspace.build());

            CreateTable createTable = SchemaBuilder.createTable("baby_url", "keys")
                    .ifNotExists()
                    .withPartitionKey("key", DataTypes.TEXT);
            cqlSession.execute(createTable.build());
        }
        deleteKey(CasandraKeyGeneratorRepositoryTest.KEY_VALUE);
    }

    @Test
    void shouldInsertKey() {
        CasandraKeyGeneratorRepository casandraKeyGeneratorRepository = new CasandraKeyGeneratorRepository(cqlSession);
        boolean isInserted = casandraKeyGeneratorRepository.insertKey(KEY_VALUE);
        assertTrue(isInserted);
        assertTrue(cassandra.isRunning());
    }

    @Test
    void shouldNotInsertRecordIfKeysAlreadyExists() {
        CasandraKeyGeneratorRepository casandraKeyGeneratorRepository = new CasandraKeyGeneratorRepository(cqlSession);
        casandraKeyGeneratorRepository.insertKey(KEY_VALUE);

        boolean isInserted = casandraKeyGeneratorRepository.insertKey(KEY_VALUE);
        assertFalse(isInserted);
    }

    private void deleteKey(String keyValue) {
        SimpleStatement statement = QueryBuilder.deleteFrom("baby_url", "keys")
                .whereColumn("key").isEqualTo(QueryBuilder.bindMarker())
                .build();
        BoundStatement boundStatement = cqlSession.prepare(statement)
                .bind().setString(0, keyValue);
        cqlSession.execute(boundStatement);
    }


}