package org.seariver.kanbanboard.read.adapter.out;

import helper.DataSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadBucketRepositoryImplTest extends DataSourceHelper {

    private ReadBucketRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new ReadBucketRepositoryImpl(dataSource);
    }

    @Test()
    void WHEN_QueryAllBucket_MUST_RetrieveSuccessful() {

        // when
        List<BucketDto> actual = repository.findAll();

        // then
        var expected = new BucketDto(UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db"), 100, "EXISTENT");
        assertThat(actual)
            .containsExactlyInAnyOrderElementsOf(List.of(expected));
    }
}
