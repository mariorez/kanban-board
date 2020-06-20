package org.seariver.kanbanboard.read.adapter.out;

import helper.DataSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.seariver.kanbanboard.read.domain.core.ReadBucketRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadBucketRepositoryImplTest extends DataSourceHelper {

    private ReadBucketRepositoryImpl repository;

    public ReadBucketRepositoryImplTest() {
        dataSetName = "ReadBucketRepositoryImplTest";
    }

    @BeforeEach
    void setup() {
        repository = new ReadBucketRepositoryImpl(dataSource);
    }

    @Test
    void MUST_ImplementInterface() {
        assertThat(repository).isInstanceOf(ReadBucketRepository.class);
    }

    @Test()
    void WHEN_QueryAllBucket_MUST_RetrieveSuccessful() {

        // when
        List<BucketDto> actual = repository.findAll();

        // then
        var expected = List.of(
            new BucketDto(UUID.fromString("6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e"), 100.15, "FIRST-BUCKET"),
            new BucketDto(UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db"), 200.987, "SECOND-BUCKET")
        );

        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
