package org.seariver.kanbanboard.read.adapter.out;

import helper.DataSourceHelper;
import helper.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.seariver.kanbanboard.read.domain.core.ReadBucketRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class ReadBucketRepositoryImplTest extends TestHelper {

    private ReadBucketRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new ReadBucketRepositoryImpl(new DataSourceHelper());
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
        assertThat(actual)
                .extracting(BucketDto::getName)
                .containsExactlyElementsOf(List.of("FIRST-BUCKET", "SECOND-BUCKET"));
    }
}
