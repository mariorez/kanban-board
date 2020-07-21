package org.seariver.kanbanboard.read.adapter.out;

import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.seariver.kanbanboard.read.domain.core.CardDto;
import org.seariver.kanbanboard.read.domain.core.ReadBucketRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                b.external_id bucket_external_id, b.position bucket_position, b.name bucket_name,
                c.external_id card_external_id, c.position card_position, c.name card_name
            FROM bucket AS b
                LEFT JOIN card AS c ON c.bucket_id = b.id
            ORDER BY b.position ASC, c.position ASC
            """;

        return jdbcTemplate.query(sql, rs -> {

            Map<Double, BucketDto> resultMap = new LinkedHashMap<>();

            while (rs.next()) {

                var position = rs.getDouble("bucket_position");

                var bucketDto = resultMap.getOrDefault(position, new BucketDto(
                    UUID.fromString(rs.getString("bucket_external_id")),
                    position,
                    rs.getString("bucket_name")));

                if (Optional.ofNullable(rs.getString("card_external_id")).isPresent()) {
                    bucketDto.addCard(new CardDto(
                        UUID.fromString(rs.getString("card_external_id")),
                        rs.getDouble("card_position"),
                        rs.getString("card_name")));
                }

                resultMap.put(position, bucketDto);
            }

            return new ArrayList<>(resultMap.values());
        });
    }
}
