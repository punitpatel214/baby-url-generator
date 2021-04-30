package example.systemdesign.babyurl.idgenerator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import example.systemdesign.babyurl.idgenerator.config.PostgresDBConfig;
import io.micronaut.context.annotation.Factory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.Properties;

@Factory
public class DataSourceFactory {

    @Inject
    private PostgresDBConfig postgresDBConfig;

    @Singleton
    public DataSource dataSource() {
        Properties props = new Properties();
        props.setProperty("dataSource.user", postgresDBConfig.getUser());
        props.setProperty("dataSource.password", postgresDBConfig.getPassword());
        props.setProperty("dataSource.databaseName", postgresDBConfig.getDatabaseName());
        HikariConfig config = new HikariConfig(props);
        config.setJdbcUrl(postgresDBConfig.getUrl());
        config.setSchema(postgresDBConfig.getSchemaName());
        return new HikariDataSource(config);
    }
}
