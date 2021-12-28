package com.babyurl.urlshortener.repositiry.cassandra;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.model.ShortenURLData;
import com.babyurl.urlshortener.repositiry.ShortenURLRepository;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.babyurl.urlshortener.repositiry.cassandra.ShortenURLTableMetaData.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
import static java.time.LocalDateTime.ofInstant;

@Singleton
public class CassandraShortenURLRepository implements ShortenURLRepository {

    private final CqlSession cqlSession;

    public CassandraShortenURLRepository(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    @Override
    public boolean save(ShortenURLData urlData) {
        SimpleStatement insert = insertStatement(urlData);
        Row one = cqlSession.execute(insert).one();
        return one != null && one.getBoolean(0);
    }

    private SimpleStatement insertStatement(ShortenURLData urlData) {
        return insertInto(TABLE_NAME)
                .value(KEY.columnName, literal(urlData.key))
                .value(URL.columnName, literal(urlData.originalURL))
                .value(EXPIRY_TIME.columnName, literal(toInstant(urlData.expiryTime)))
                .value(CREATE_TIME.columnName, literal(toInstant(urlData.createTime)))
                .ifNotExists()
                .build();
    }

    @Override
    public Optional<Redirection> find(String key) {
        SimpleStatement statement = selectStatement(key);
        ResultSet resultSet = cqlSession.execute(statement);
        return Optional.ofNullable(resultSet.one())
                .map(this::mapToRedirection);
    }

    public  SimpleStatement selectStatement(String key) {
        return selectFrom(TABLE_NAME)
                .column(KEY.columnName).column(URL.columnName).column(EXPIRY_TIME.columnName)
                .whereColumn(KEY.columnName).isEqualTo(literal(key))
                .build();
    }

    public Redirection mapToRedirection(Row row) {
        Instant expiryTime = row.getInstant(EXPIRY_TIME.columnName);
        return new Redirection(row.getString(KEY.columnName), row.getString(URL.columnName), toLocalDateTime(expiryTime));
    }

    private Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return ofInstant(instant, ZoneId.systemDefault());
    }
}
