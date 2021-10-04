package com.babyurl.keygeneator.repository.casandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetSocketAddress;
import java.time.Duration;

public class BaseCassandraContainerTest {
    private static final CassandraContainer<?> cassandraContainer = new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
            .withExposedPorts(9042)
            .withStartupTimeout(Duration.ofMinutes(3));

    protected static final String USED_KEYS = "usedKeys";
    protected static final String KEYS = "keys";
    protected CqlSession cqlSession;

    protected static void startCassandraContainer() {
        if (cassandraContainer.isRunning()) {
            return;
        }
        cassandraContainer.start();
    }

    protected void dataSetup() {
        if (cqlSession == null) {
            cqlSession = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(cassandraContainer.getContainerIpAddress(), cassandraContainer.getMappedPort(9042)))
                    .withLocalDatacenter("datacenter1")
                    .build();
            createKeySpace();
            createTable(KEYS);
            createTable(USED_KEYS);
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
}
