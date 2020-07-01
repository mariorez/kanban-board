package org.seariver.kanbanboard.write.adapter.out;

import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.exception.DuplicatedDataException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.seariver.kanbanboard.write.domain.exception.DomainException.Error.INVALID_DUPLICATED_DATA;

@Repository
public class WriteBucketRepositoryImpl implements WriteBucketRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public WriteBucketRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void create(Bucket bucket) {

        try {
            var sql = """
                INSERT INTO bucket(uuid, position, name)
                values (:uuid, :position, :name)""";

            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("uuid", bucket.getUuid())
                .addValue("position", bucket.getPosition())
                .addValue("name", bucket.getName());

            jdbcTemplate.update(sql, parameters);

        } catch (DuplicateKeyException exception) {

            var duplicatedDataException = new DuplicatedDataException(INVALID_DUPLICATED_DATA, exception);

            var existentBuckets = findByUuidOrPosition(bucket.getUuid(), bucket.getPosition());

            existentBuckets.forEach(existentBucket -> {

                if (existentBucket.getUuid().equals(bucket.getUuid())) {
                    duplicatedDataException.addError("id", bucket.getUuid());
                }

                if (existentBucket.getPosition() == bucket.getPosition()) {
                    duplicatedDataException.addError("position", bucket.getPosition());
                }
            });

            throw duplicatedDataException;
        }
    }

    @Override
    public void update(Bucket bucket) {

        var sql = """
            UPDATE bucket
            SET position = :position, name =:name
            WHERE uuid = :uuid""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("uuid", bucket.getUuid())
            .addValue("position", bucket.getPosition())
            .addValue("name", bucket.getName());

        jdbcTemplate.update(sql, parameters);
    }

    public Optional<Bucket> findByUuid(UUID uuid) {

        var sql = """
            SELECT id, uuid, position, name, created_at, updated_at
            FROM bucket
            WHERE uuid = :uuid""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("uuid", uuid);

        return jdbcTemplate.query(sql, parameters, resultSet -> {

            if (resultSet.next()) {
                return Optional.of(new Bucket()
                    .setId(resultSet.getLong("id"))
                    .setUuid(UUID.fromString(resultSet.getString("uuid")))
                    .setPosition(resultSet.getDouble("position"))
                    .setName(resultSet.getString("name"))
                    .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                );
            }

            return Optional.empty();
        });
    }

    public List<Bucket> findByUuidOrPosition(UUID uuid, double position) {

        var sql = """
            SELECT id, uuid, position, name, created_at, updated_at
            FROM bucket
            WHERE uuid = :uuid OR position = :position""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("uuid", uuid)
            .addValue("position", position);

        return jdbcTemplate.query(sql, parameters, (rs, rowNum) ->
            new Bucket()
                .setId(rs.getLong("id"))
                .setUuid(UUID.fromString(rs.getString("uuid")))
                .setPosition(rs.getDouble("position"))
                .setName(rs.getString("name"))
                .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                .setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
        );
    }
}
