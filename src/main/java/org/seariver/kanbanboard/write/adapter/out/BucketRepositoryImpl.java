package org.seariver.kanbanboard.write.adapter.out;

import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.BucketRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BucketRepositoryImpl implements BucketRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public BucketRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void create(Bucket bucket) {

        String sql = """
            INSERT INTO bucket(uuid, position, name)
            values (:uuid, :position, :name)""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("uuid", bucket.getUuid())
            .addValue("position", bucket.getPosition())
            .addValue("name", bucket.getName());

        jdbcTemplate.update(sql, parameters);
    }

    public Optional<Bucket> findByUuid(UUID id) {
        String sql = """
            SELECT uuid, position, name
            FROM bucket
            WHERE uuid = :uuid""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("uuid", id);

        return jdbcTemplate.query(sql, parameters, resultSet -> {
            if (resultSet.next()) {
                return Optional.of(new Bucket().
                    setUuid(UUID.fromString(resultSet.getString("uuid"))).
                    setPosition(resultSet.getInt("position")).
                    setName(resultSet.getString("name"))
                );
            }

            return Optional.empty();
        });
    }
}
