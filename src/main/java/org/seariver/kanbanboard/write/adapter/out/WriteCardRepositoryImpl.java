package org.seariver.kanbanboard.write.adapter.out;

import org.seariver.kanbanboard.write.domain.core.Card;
import org.seariver.kanbanboard.write.domain.core.WriteCardRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WriteCardRepositoryImpl implements WriteCardRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public WriteCardRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void create(Card card) {
        var sql = """
            INSERT INTO card (bucket_id, external_id, position, name)
            values (:bucket_id, :externalId, :position, :name)""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("bucket_id", card.getBucketId())
            .addValue("externalId", card.getExternalId())
            .addValue("position", card.getPosition())
            .addValue("name", card.getName());

        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public Optional<Card> findByExternalId(UUID externalId) {

        var sql = """
            SELECT bucket_id, external_id, position, name, created_at, updated_at
            FROM card
            WHERE external_id = :externalId""";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("externalId", externalId);

        return jdbcTemplate.query(sql, parameters, resultSet -> {

            if (resultSet.next()) {
                return Optional.of(new Card()
                    .setBucketId(resultSet.getLong("bucket_id"))
                    .setExternalId(UUID.fromString(resultSet.getString("external_id")))
                    .setPosition(resultSet.getDouble("position"))
                    .setName(resultSet.getString("name"))
                    .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                );
            }

            return Optional.empty();
        });
    }
}
