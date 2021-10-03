package com.babyurl.repository.casandra;

import com.babyurl.repository.KeyGeneratorRepository;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import jakarta.inject.Singleton;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Singleton
public class CassandraKeyGeneratorRepository implements KeyGeneratorRepository {

    private static final String KEY_SPACE = "baby_url";
    private static final String KEYS_TABLE = "keys";
    private final CqlSession cqlSession;

    public CassandraKeyGeneratorRepository(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    @Override
    public boolean insertKey(String key) {
        Insert insert = insertInto(KEY_SPACE, KEYS_TABLE)
                .value("key", literal(key))
                .ifNotExists();
        return cqlSession.execute(insert.build()).all()
                .stream()
                .findFirst()
                .map(row -> row.getBoolean(0))
                .orElse(false);
    }

    @Override
    public String getKey() {
        return fetchRandomKey()
                .filter(this::insertInUsedKey)
                .filter(this::deleteKey)
                .orElseThrow(NoSuchElementException::new);
    }

    private Optional<String> fetchRandomKey() {
        Select select = QueryBuilder.selectFrom(KEY_SPACE, KEYS_TABLE)
                .column("key")
                .limit(1);
        return cqlSession.execute(select.build()).all()
                .stream()
                .findFirst()
                .map(row -> row.getString("key"));
    }

    private boolean insertInUsedKey(String key) {
        Insert insert = insertInto(KEY_SPACE, "usedKeys")
                .value("key", literal(key))
                .ifNotExists();
        return cqlSession.execute(insert.build()).all()
                .stream()
                .findFirst()
                .map(row -> row.getBoolean(0))
                .orElse(false);
    }

    private boolean deleteKey(String key) {
        Delete delete = QueryBuilder.deleteFrom(KEY_SPACE, KEYS_TABLE)
                .whereColumn("key").isEqualTo(literal(key))
                .ifExists();
        return cqlSession.execute(delete.build()).all()
                .stream()
                .findFirst()
                .map(row -> row.getBoolean(0))
                .orElse(false);
    }
}
