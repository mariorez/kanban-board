package org.seariver.kanbanboard.write.adapter.out;

import helper.DataSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.exception.DuplicatedDataException;
import org.seariver.kanbanboard.write.domain.core.Bucket;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class WriteBucketRepositoryImplTest extends DataSourceHelper {

    private WriteBucketRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new WriteBucketRepositoryImpl(dataSource);
    }

    @Test
    void MUST_ImplementInterface() {
        assertThat(repository).isInstanceOf(WriteBucketRepository.class);
    }

    @ParameterizedTest
    @MethodSource("validDataProvider")
    void GIVEN_ValidBucket_MUST_PersistOnDatabase(UUID id,
                                                  double position,
                                                  String name) {
        // given
        var expected = new Bucket()
            .setUuid(id)
            .setPosition(position)
            .setName(name);

        // when
        repository.create(expected);

        // then
        var actualOptional = repository.findByUuid(id);
        Bucket actual = actualOptional.get();
        assertThat(actual.getUuid()).isEqualTo(expected.getUuid());
        assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    void GIVEN_AlreadyExistentBucket_MUST_ThrowException(UUID id,
                                                         double position,
                                                         String name,
                                                         Map<String, Object> expectedError) {
        // given
        var expected = new Bucket()
            .setUuid(id)
            .setPosition(position)
            .setName(name);

        // then
        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class, () -> repository.create(expected));

        // when
        assertThat(exception.getMessage()).isEqualTo("Invalid duplicated data");
        assertThat(exception.getErrors()).containsExactlyInAnyOrderEntriesOf(expectedError);
    }

    private static Stream<Arguments> validDataProvider() {
        return Stream.of(
            arguments(UUID.randomUUID(), 1, "TODO"),
            arguments(UUID.randomUUID(), 2.35, "EXISTENT")
        );
    }

    private static Stream<Arguments> invalidDataProvider() {

        UUID existentUuid = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
        double existentPosition = 100.15;

        return Stream.of(
            arguments(existentUuid, 1, "TODO", Map.of("id", existentUuid)),
            arguments(UUID.randomUUID(), existentPosition, "DOING", Map.of("position", existentPosition)),
            arguments(existentUuid, existentPosition, "DONE", Map.of("id", existentUuid, "position", Double.valueOf(existentPosition)))
        );
    }
}