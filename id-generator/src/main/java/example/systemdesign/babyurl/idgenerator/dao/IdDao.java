package example.systemdesign.babyurl.idgenerator.dao;

import example.systemdesign.babyurl.idgenerator.config.ApplicationConfig;
import example.systemdesign.babyurl.idgenerator.domain.IdGenerator;
import example.systemdesign.babyurl.idgenerator.exception.DataAccessException;
import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static example.systemdesign.babyurl.idgenerator.domain.IdStatus.READY_TO_USE;
import static example.systemdesign.babyurl.idgenerator.domain.IdStatus.USED;

@Singleton
public class IdDao {
    private static final String COUNT_QUERY = "select count(*) as count from id_detail where status = ?";
    private static final String INSERT_QUERY = "insert into id_detail(id, status, instance_id) values (?,?,?) on conflict do nothing";
    private static final String GET_ID_QUERY="WITH cte AS (" +
            "   SELECT serial_number" +
            "   FROM   id_detail" +
            "   WHERE  status = ?" +
            "   LIMIT  ?" +
            "   )" +
            " UPDATE id_detail idDetail" +
            " SET    status = ?" +
            " FROM   cte" +
            " WHERE  idDetail.serial_number = cte.serial_number and idDetail.status=?" +
            " RETURNING idDetail.id";

    private final DataSource dataSource;
    private final int batchSize;
    private ApplicationConfig applicationConfig;

    public IdDao(DataSource dataSource, ApplicationConfig applicationConfig) {
        this.dataSource = dataSource;
        this.batchSize = applicationConfig.getBatchSize();
        this.applicationConfig = applicationConfig;
    }

    public long getReadyToUseIdCount() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(COUNT_QUERY)) {
            ps.setString(1, READY_TO_USE.name());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("count");
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public long generateAndInsertIds(IdGenerator idGenerator, long count) {
        long maxAttempt = (count/batchSize) * 2;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            long idCount = getReadyToUseIdCount();
            while (idCount < count) {
                long batch = Math.min(batchSize, (count - idCount));
                for (int i = 0; i < batch; i++) {
                    ps.clearParameters();
                    ps.setString(1, idGenerator.generateId());
                    ps.setString(2, READY_TO_USE.name());
                    ps.setString(3, "instance_1");
                    ps.addBatch();
                }
                ps.executeBatch();
                idCount = getReadyToUseIdCount();
                if (--maxAttempt < 0) {
                    break;
                }
            }
            return idCount;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public List<String> getIds(int count) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ID_QUERY)) {
            ps.setString(1, READY_TO_USE.name());
            ps.setInt(2, count);
            ps.setString(3, USED.name());
            ps.setString(4, READY_TO_USE.name());
            try (ResultSet resultSet = ps.executeQuery()) {
                List<String> ids = new ArrayList<>();
                while (resultSet.next()) {
                    ids.add(resultSet.getString("id"));
                }
                return ids;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }


}
