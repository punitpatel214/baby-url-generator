package com.babyurl.keygeneator.repository.casandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

public abstract class BaseCassandraContainerTest implements TestPropertyProvider {
    protected static final CassandraContainer<?> cassandraContainer = new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
            .withExposedPorts(9042)
            .withStartupTimeout(Duration.ofMinutes(3));

    protected static final String USED_KEYS = "usedKeys";
    protected static final String KEYS = "keys";
    private static String KEY_SPACE_NAME = "key_generator";

    private void startContainer() {
        if (cassandraContainer.isRunning()) {
            return;
        }
        cassandraContainer.start();
        createKeySpace(KEY_SPACE_NAME, CqlSession.builder()
                .addContactPoint(new InetSocketAddress(cassandraContainer.getContainerIpAddress(), cassandraContainer.getMappedPort(9042)))
                .withLocalDatacenter("datacenter1")
                .build());
    }

    protected void dataSetup(CqlSession cqlSession) {
        createTable(KEYS, cqlSession);
        createTable(USED_KEYS, cqlSession);
    }

    private void createKeySpace(String keySpace, CqlSession cqlSession) {
        CreateKeyspace createKeyspace = createKeyspace(keySpace)
                .ifNotExists()
                .withSimpleStrategy(1);

        cqlSession.execute(createKeyspace.build());
    }

    private void createTable(String tableName, CqlSession cqlSession) {
        CreateTable createTable = SchemaBuilder.createTable(tableName)
                .ifNotExists()
                .withPartitionKey("key", DataTypes.TEXT);
        cqlSession.execute(createTable.build());
    }

    @Override
    @NonNull
    public Map getProperties() {
        startContainer();
        String contactPoints = cassandraContainer.getContainerIpAddress() + ":" + cassandraContainer.getMappedPort(9042);
        return Map.of("cassandra.default.basic.contact-points", Collections.singletonList(contactPoints));
    }
}
