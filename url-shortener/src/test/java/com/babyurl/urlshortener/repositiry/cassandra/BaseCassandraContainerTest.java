package com.babyurl.urlshortener.repositiry.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static com.babyurl.urlshortener.repositiry.cassandra.ShortenURLTableMetaData.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static java.util.Collections.singletonList;

public abstract class BaseCassandraContainerTest implements TestPropertyProvider {
    protected static final CassandraContainer<?> cassandraContainer = new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
            .withExposedPorts(9042)
            .withStartupTimeout(Duration.ofMinutes(3));

    private static final String KEY_SPACE_NAME = "url_shortener";

    private void startContainer() {
        if (cassandraContainer.isRunning()) {
            return;
        }
        cassandraContainer.start();
        getCqlSession().execute(createTableStatement());
    }

    private CqlSession getCqlSession() {
        CqlSession cqlSession = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(cassandraContainer.getContainerIpAddress(), cassandraContainer.getMappedPort(9042)))
                .withLocalDatacenter("datacenter1")
                .build();
        createKeySpace(KEY_SPACE_NAME, cqlSession);
        cqlSession.execute("use " + KEY_SPACE_NAME);
        return cqlSession;
    }

    private void createKeySpace(String keySpace, CqlSession cqlSession) {
        CreateKeyspace createKeyspace = createKeyspace(keySpace)
                .ifNotExists()
                .withSimpleStrategy(1);
        cqlSession.execute(createKeyspace.build());
    }

    private SimpleStatement createTableStatement() {
        return createTable(TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(KEY.columnName, KEY.dataType)
                .withColumn(URL.columnName, URL.dataType)
                .withColumn(CREATE_TIME.columnName, CREATE_TIME.dataType)
                .withColumn(EXPIRY_TIME.columnName, EXPIRY_TIME.dataType)
                .build();
    }

    protected void expireUrl(String value) {
        getCqlSession().execute(update(TABLE_NAME)
                .setColumn(EXPIRY_TIME.columnName, literal(Instant.now().minusSeconds(1)))
                .whereColumn(KEY.columnName)
                .isEqualTo(literal(value))
                .build());
    }

    protected void deleteAllURLS() {
        getCqlSession().execute(QueryBuilder.truncate(TABLE_NAME).build());
    }

    @Override
    @NonNull
    public Map getProperties() {
        startContainer();
        String contactPoints = cassandraContainer.getContainerIpAddress() + ":" + cassandraContainer.getMappedPort(9042);
        return Map.of("cassandra.default.basic.contact-points", singletonList(contactPoints));
    }

}
