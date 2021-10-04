package com.babyurl.keygeneator.repository.casandra;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CassandraKeyGeneratorRepositoryTest extends BaseCassandraContainerTest {

    private static final String KEY_VALUE = "randomKey";

    @BeforeAll
    static void beforeAll() {
        startCassandraContainer();
    }

    @BeforeEach
    void setUp() {
        super.dataSetup();
        deleteAll(KEYS);
        deleteAll(USED_KEYS);
    }


    @Test
    void shouldInsertKey() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        boolean isInserted = cassandraKeyGeneratorRepository.insertKey(KEY_VALUE);
        assertTrue(isInserted);
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

        String usedKeys = getKeysFromTable(USED_KEYS).get(0);
        assertEquals(KEY_VALUE, usedKeys);

        List<String> keys = getKeysFromTable(KEYS);
        assertTrue(keys.isEmpty());
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

    @Test
    void shouldGetKeys() {
        CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository = new CassandraKeyGeneratorRepository(cqlSession);
        IntStream.range(0, 10)
                .forEach(index -> cassandraKeyGeneratorRepository.insertKey(KEY_VALUE + index));

        List<String> keys = cassandraKeyGeneratorRepository.getKeys(10);
        assertEquals(10, keys.size());

        List<String> usedKeys = getKeysFromTable(USED_KEYS);
        assertEquals(10, usedKeys.size());

        keys = getKeysFromTable(KEYS);
        assertTrue(keys.isEmpty());
    }

    private List<String> getKeysFromTable(String tableName) {
        Select select = QueryBuilder.selectFrom(tableName)
                .column("key");
        return cqlSession.execute(select.build()).all().stream()
                .map(resultSet -> resultSet.getString("key"))
                .collect(Collectors.toList());
    }

    private void deleteAll(String tableName) {
        cqlSession.execute(QueryBuilder.truncate(tableName).build());
    }

}