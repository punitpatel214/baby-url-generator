package com.babyurl.repository.casandra;

import com.babyurl.repository.KeyGeneratorRepository;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import jakarta.inject.Singleton;

@Singleton
public class CasandraKeyGeneratorRepository implements KeyGeneratorRepository {

    private final CqlSession cqlSession;

    public CasandraKeyGeneratorRepository(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    @Override
    public boolean insertKey(String key) {
        SimpleStatement statement = QueryBuilder.insertInto("baby_url", "keys")
                .value("key", QueryBuilder.bindMarker())
                .ifNotExists()
                .build();
        BoundStatement boundStatement = cqlSession.prepare(statement).bind()
                .setString(0, key);
        ResultSet resultSet = cqlSession.execute(boundStatement);
        Row one = resultSet.one();
        return one != null && one.getBoolean(0);
    }
}
