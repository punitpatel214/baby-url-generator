package com.babyurl.repository.casandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.NoSuchElementException;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static org.junit.jupiter.api.Assertions.*;

class CassandraKeyGeneratorRepositoryTest {

    private static final String EMPTY_STRING = "";
    private static final CassandraContainer<?> cassandraContainer = new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
            .withExposedPorts(9042)
            .withStartupTimeout(Duration.ofMinutes(3));

    @BeforeAll
    static void beforeAll() {
        cassandraContainer.start();
    }

    private CqlSession cqlSession;

    private static final String KEY_VALUE = "randomKey";

    @BeforeEach
    void setUp() {
        dataSetup();
        deleteKey(CassandraKeyGeneratorRepositoryTest.KEY_VALUE);
    }

    private void dataSetup() {
        if (cqlSession == null) {
            cqlSession = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(cassandraContainer.getContainerIpAddress(), cassandraContainer.getMappedPort(9042)))
                    .withLocalDatacenter("datacenter1")
                    .build();
            createKeySpace();
            createTable("keys");
            createTable("usedKeys");
        }
    }

    private void createKeySpace() {
        CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace("baby_url")
                .ifNotExists()
                .withSimpleStrategy(1);

        cqlSession.execute(createKeyspace.build());
        cqlSession.execute("use baby_url");
    }

    private void createTable(String tableName) {
        CreateTable createTable = SchemaBuilder.createTable(tableName)
                .ifNotExists()
                .withPartitionKey("key", DataTypes.TEXT);
        cqlSession.execute(createTable.build());
    }

    @Test
    void shouldInsertKey() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        boolean isInserted = cassandraKeyGeneratorRepository.insertKey(KEY_VALUE);
        assertTrue(isInserted);
        assertTrue(cassandraContainer.isRunning());
    }

    @Test
    void shouldNotInsertRecordIfKeysAlreadyExists() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        cassandraKeyGeneratorRepository.insertKey(KEY_VALUE);

        boolean isInserted = cassandraKeyGeneratorRepository.insertKey(KEY_VALUE);
        assertFalse(isInserted);
    }

    @Test
    void shouldGetKeyReturnKeyAndInsertKeyToUsedKeysAndRemoveKeyFromKeys() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        cassandraKeyGeneratorRepository.insertKey(KEY_VALUE);

        String key = cassandraKeyGeneratorRepository.getKey();
        assertEquals(KEY_VALUE, key);

        String usedKeys = getKeyFromTable("usedKeys");
        assertEquals(KEY_VALUE, usedKeys);

        String deleteKey = getKeyFromTable("keys");
        assertEquals(EMPTY_STRING, deleteKey);
    }

    @Test
    void shouldThrowKeyNotFoundExceptionWhenKeyIsNotAvailable() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        assertThrows(NoSuchElementException.class, cassandraKeyGeneratorRepository::getKey);
    }

    @Test
    void shouldThrowKeyNotFoundExceptionWhenKeyIsNotInsertedInUsedKey() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        cassandraKeyGeneratorRepository.insertKey(KEY_VALUE);
        cqlSession.execute(String.format("insert into %s.usedKeys(key) values ('%s')", "baby_url", KEY_VALUE));

        assertThrows(NoSuchElementException.class, cassandraKeyGeneratorRepository::getKey);
    }

    private String getKeyFromTable(String tableName) {
        Select select = QueryBuilder.selectFrom(tableName)
                .column("key")
                .whereColumn("key").isEqualTo(literal(KEY_VALUE));
        return cqlSession.execute(select.build()).all().stream().findFirst()
                .map(resultSet -> resultSet.getString("key"))
                .orElse(EMPTY_STRING);
    }

    private void deleteKey(String keyValue) {
        Delete delete = QueryBuilder.deleteFrom("keys")
                .whereColumn("key").isEqualTo(literal(keyValue));
        cqlSession.execute(delete.build());
    }


}