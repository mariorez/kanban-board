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
                    INSERT INTO bucket(%s, %s, %s)
                    values (:%s, :%s, :%s)""".formatted(
                    EXTERNAL_ID_FIELD, POSITION_FIELD, NAME_FIELD,
                    EXTERNAL_ID_FIELD, POSITION_FIELD, NAME_FIELD);

            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue(EXTERNAL_ID_FIELD, bucket.getExternalId())
                    .addValue(POSITION_FIELD, bucket.getPosition())
                    .addValue(NAME_FIELD, bucket.getName());

            jdbcTemplate.update(sql, parameters);

        } catch (DuplicateKeyException exception) {

            var duplicatedDataException = new DuplicatedDataException(INVALID_DUPLICATED_DATA, exception);

            var existentBuckets = findByExternalIdOrPosition(bucket.getExternalId(), bucket.getPosition());

            existentBuckets.forEach(existentBucket -> {

                if (existentBucket.getExternalId().equals(bucket.getExternalId())) {
                    duplicatedDataException.addError("id", bucket.getExternalId());
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
                SET
                    %s = :%s,
                    %s = :%s
                WHERE %s = :%s""".formatted(
                POSITION_FIELD, POSITION_FIELD,
                NAME_FIELD, NAME_FIELD,
                EXTERNAL_ID_FIELD, EXTERNAL_ID_FIELD);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(EXTERNAL_ID_FIELD, bucket.getExternalId())
                .addValue(POSITION_FIELD, bucket.getPosition())
                .addValue(NAME_FIELD, bucket.getName());

        jdbcTemplate.update(sql, parameters);
    }

    public Optional<Bucket> findByExteranlId(UUID externalId) {

        var sql = """
                SELECT %s, %s, %s, %s, %s, %s
                FROM bucket
                WHERE %s = :%s""".formatted(
                ID_FIELD, EXTERNAL_ID_FIELD, POSITION_FIELD, NAME_FIELD, CREATED_AT_FIELD, UPDATED_AT_FIELD,
                EXTERNAL_ID_FIELD, EXTERNAL_ID_FIELD);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(EXTERNAL_ID_FIELD, externalId);

        return jdbcTemplate.query(sql, parameters, resultSet -> {

            if (resultSet.next()) {
                return Optional.of(new Bucket()
                        .setId(resultSet.getLong(ID_FIELD))
                        .setExternalId(UUID.fromString(resultSet.getString(EXTERNAL_ID_FIELD)))
                        .setPosition(resultSet.getDouble(POSITION_FIELD))
                        .setName(resultSet.getString(NAME_FIELD))
                        .setCreatedAt(resultSet.getTimestamp(CREATED_AT_FIELD).toLocalDateTime())
                        .setUpdatedAt(resultSet.getTimestamp(UPDATED_AT_FIELD).toLocalDateTime())
                );
            }

            return Optional.empty();
        });
    }

    public List<Bucket> findByExternalIdOrPosition(UUID externalId, double position) {

        var sql = """
                SELECT %S, %S, %S, %S, %S, %S
                FROM bucket
                WHERE
                    %s = :%s OR
                    %s = :%s""".formatted(
                ID_FIELD, EXTERNAL_ID_FIELD, POSITION_FIELD, NAME_FIELD, CREATED_AT_FIELD, UPDATED_AT_FIELD,
                EXTERNAL_ID_FIELD, EXTERNAL_ID_FIELD,
                POSITION_FIELD, POSITION_FIELD);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(EXTERNAL_ID_FIELD, externalId)
                .addValue(POSITION_FIELD, position);

        return jdbcTemplate.query(sql, parameters, (resultSet, rowNum) ->
                new Bucket()
                        .setId(resultSet.getLong(ID_FIELD))
                        .setExternalId(UUID.fromString(resultSet.getString(EXTERNAL_ID_FIELD)))
                        .setPosition(resultSet.getDouble(POSITION_FIELD))
                        .setName(resultSet.getString(NAME_FIELD))
                        .setCreatedAt(resultSet.getTimestamp(CREATED_AT_FIELD).toLocalDateTime())
                        .setUpdatedAt(resultSet.getTimestamp(UPDATED_AT_FIELD).toLocalDateTime())
        );
    }
}
