package org.seariver.kanbanboard.read.adapter.out;

import org.seariver.kanbanboard.read.domain.core.BucketDto;
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

        var sql = "SELECT uuid, position, name FROM bucket ORDER BY position";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new BucketDto(
                UUID.fromString(rs.getString("uuid")),
                rs.getDouble("position"),
                rs.getString("name"))
        );
    }
}
