package org.seariver.kanbanboard.read.adapter.out;

import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.seariver.kanbanboard.read.domain.core.CardDto;
import org.seariver.kanbanboard.read.domain.core.ReadBucketRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class ReadBucketRepositoryImpl implements ReadBucketRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public ReadBucketRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<BucketDto> findAll() {

        var sql = """
            SELECT
                b.uuid b_uuid, b.position b_position, b.name b_name,
                c.uuid c_uuid, c.position c_position, c.name c_name
            FROM
                bucket AS b
                LEFT JOIN card AS c ON c.bucket_id = b.id\s
            ORDER BY b.position, c.position;
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->

            new BucketDto(
                UUID.fromString(rs.getString("b_uuid")),
                rs.getDouble("b_position"),
                rs.getString("b_name"),
                List.of(new CardDto(
                    UUID.fromString(rs.getString("c_uuid")),
                    rs.getDouble("c_position"),
                    rs.getString("c_name")
                )))
        );
    }
}
