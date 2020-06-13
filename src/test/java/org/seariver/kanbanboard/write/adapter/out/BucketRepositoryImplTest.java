package org.seariver.kanbanboard.write.adapter.out;

import helper.DataSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.BucketRepository;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BucketRepositoryImplTest extends DataSourceHelper {

    private BucketRepository repository;

    @BeforeEach
    void setup() {
        repository = new BucketRepositoryImpl(dataSource);
    }

    @ParameterizedTest
    @MethodSource("validDataProvider")
    void GIVEN_ValidBucket_MUST_PersistOnDatabase(UUID id,
                                                  int position,
                                                  String name) {
        // given
        var expected = new Bucket()
            .setUuid(id)
            .setPosition(position)
            .setName(name);

        // when
        repository.create(expected);

        // then
        var actual = repository.findByUuid(id);
        assertThat(actual.get()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    void GIVEN_AlreadyExistentBucket_MUST_ThrowException(UUID id,
                                                         int position,
                                                         String name) {
        // given
        var expected = new Bucket()
            .setUuid(id)
            .setPosition(position)
            .setName(name);

        // then
        assertThrows(RuntimeException.class, () -> repository.create(expected));
    }

    private static Stream<Arguments> validDataProvider() {
        return Stream.of(
            arguments(UUID.randomUUID(), 1, "TODO"),
            arguments(UUID.randomUUID(), 2, "DOING"),
            arguments(UUID.randomUUID(), 3, "DONE")
        );
    }

    private static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
            arguments(UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db"), 1, "TODO"),
            arguments(UUID.randomUUID(), 100, "DOING"),
            arguments(UUID.randomUUID(), 2, "EXISTENT")
        );
    }
}