package com.babyurl.keygeneator.repository.casandra;

import com.babyurl.keygeneator.exception.KeyNotFoundException;
import com.babyurl.keygeneator.repository.KeyGeneratorRepository;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Singleton
public class CassandraKeyGeneratorRepository implements KeyGeneratorRepository {

    private static final String KEYS_TABLE = "keys";
    public static final String USED_KEYS = "usedKeys";
    private final CqlSession cqlSession;

    public CassandraKeyGeneratorRepository(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    @Override
    public boolean insertKey(String key) {
        Insert insert = insertInto(KEYS_TABLE)
                .value("key", literal(key))
                .ifNotExists();
        Row firstRow = cqlSession.execute(insert.build())
                .one();
        return firstRow != null && firstRow.getBoolean(0);
    }

    @Override
    public String getKey() {
        return getKeys(1).stream()
                .findFirst()
                .orElseThrow(KeyNotFoundException::new);
    }

    @Override
    public List<String> getKeys(int size) {
        return fetchRandomKey(size).stream()
                .filter(this::insertInUsedKey)
                .filter(this::deleteKey)
                .collect(Collectors.toList());
    }

    private List<String> fetchRandomKey(int limit) {
        Select select = QueryBuilder.selectFrom(KEYS_TABLE)
                .column("key")
                .limit(limit);
        return cqlSession.execute(select.build()).all()
                .stream()
                .map(row -> row.getString("key"))
                .collect(Collectors.toList());
    }

    private boolean insertInUsedKey(String key) {
        Insert insert = insertInto(USED_KEYS)
                .value("key", literal(key))
                .ifNotExists();
        return cqlSession.execute(insert.build()).all()
                .stream()
                .findFirst()
                .map(row -> row.getBoolean(0))
                .orElse(false);
    }

    private boolean deleteKey(String key) {
        Delete delete = QueryBuilder.deleteFrom(KEYS_TABLE)
                .whereColumn("key").isEqualTo(literal(key))
                .ifExists();
        return cqlSession.execute(delete.build()).all()
                .stream()
                .findFirst()
                .map(row -> row.getBoolean(0))
                .orElse(false);
    }
}
