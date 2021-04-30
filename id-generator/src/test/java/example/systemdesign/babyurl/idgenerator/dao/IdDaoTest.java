package example.systemdesign.babyurl.idgenerator.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import example.systemdesign.babyurl.idgenerator.config.ApplicationConfig;
import example.systemdesign.babyurl.idgenerator.domain.RandomIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import static example.systemdesign.babyurl.idgenerator.domain.IdStatus.READY_TO_USE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class IdDaoTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2");

    private IdDao idDao;
    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        dataSource = getDataSource();
        idDao = new IdDao(dataSource, new ApplicationConfig() {
            @Override
            public int getBatchSize() {
                return 10;
            }
        });
        String setUpQuery = readResourceFile("setup.sql");
        executeQuery(setUpQuery);
        executeQuery("delete from id_detail");
    }

    @Test
    void shouldGetReadyToUseIdCounts() {
        executeQuery("INSERT INTO id_detail(id, status, instance_id) VALUES ( 'id', '" + READY_TO_USE  +"', 0)");
        long count = idDao.getReadyToUseIdCount();
        assertEquals(1, count);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 53, 100, 1000})
    void shouldGenerateAndSaveIdsGenerateIdsAsPerCountR(int count) {
        idDao.generateAndInsertIds(new RandomIdGenerator(6), count);
        long actualCount = idDao.getReadyToUseIdCount();
        assertEquals(count, actualCount);
    }

    @Test
    void shouldGenerateIdsReturnNumberOfIdGeneratedBySystem() {
        idDao.generateAndInsertIds(() -> "id", 10);
        long actualCount = idDao.getReadyToUseIdCount();
        assertEquals(1, actualCount);
    }

    @Test
    void shouldGenerateIdGenerationFailEventWhenDesireIdNotGenerate() {
        idDao.generateAndInsertIds(() -> "id", 2);
    }

    private String readResourceFile(String fileName) throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource(fileName);
        URI uri = Objects.requireNonNull(resource).toURI();
        return new String(Files.readAllBytes(Paths.get(uri)));
    }

    private void executeQuery(String data) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(data)
            ) {
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private HikariDataSource getDataSource() {
        Properties props = new Properties();
        props.setProperty("dataSource.user", postgreSQLContainer.getUsername());
        props.setProperty("dataSource.password", postgreSQLContainer.getPassword());
        HikariConfig config = new HikariConfig(props);
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        return new HikariDataSource(config);
    }

}