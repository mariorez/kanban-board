package org.seariver.kanbanboard.write.adapter.out;

import helper.DataSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.exception.DuplicatedDataException;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class WriteBucketRepositoryImplTest extends DataSourceHelper {

    private WriteBucketRepositoryImpl repository;

    public WriteBucketRepositoryImplTest() {
        dataSetName = "WriteBucketRepositoryImplTest";
    }

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
    void WHEN_CreatingBucket_GIVEN_ValidData_MUST_PersistOnDatabase(UUID id,
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
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getUuid()).isEqualTo(expected.getUuid());
        assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    void WHEN_CreatingBucket_GIVEN_AlreadyExistentBucket_MUST_ThrowException(UUID id,
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

    @Test
    void WHEN_UpdatingBucket_WITH_ValidData_MUST_SaveOnDatabase() {
        // given
        var id = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
        var actualBucket = repository.findByUuid(id).get();
        assertThat(actualBucket.getPosition()).isEqualTo(200.987);
        assertThat(actualBucket.getName()).isEqualTo("EXISTENT NAME");

        var position = faker.number().randomDouble(3, 1, 10);
        var name = faker.pokemon().name();
        actualBucket.setPosition(position).setName(name);

        // when
        repository.update(actualBucket);

        // then
        var expectedBucket = repository.findByUuid(id).get();
        assertThat(expectedBucket.getUuid()).isEqualTo(id);
        assertThat(expectedBucket.getPosition()).isEqualTo(position);
        assertThat(expectedBucket.getName()).isEqualTo(name);
        assertThat(expectedBucket.getCreatedAt()).isNotNull();
        assertThat(expectedBucket.getUpdatedAt()).isNotNull();
    }

    private static Stream<Arguments> validDataProvider() {
        return Stream.of(
            arguments(UUID.randomUUID(),
                faker.number().randomDigitNotZero(),
                faker.pokemon().name()),
            arguments(UUID.randomUUID(),
                faker.number().randomDouble(3, 1, 10),
                "EXISTENT NAME")
        );
    }

    private static Stream<Arguments> invalidDataProvider() {

        var existentUuid = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
        var existentPosition = 200.987;
        var whateverName = faker.pokemon().name();

        return Stream.of(
            arguments(existentUuid,
                faker.number().randomDouble(3, 1, 10),
                whateverName,
                Map.of("id", existentUuid)),
            arguments(UUID.randomUUID(),
                existentPosition,
                whateverName,
                Map.of("position", existentPosition)),
            arguments(existentUuid,
                existentPosition,
                whateverName,
                Map.of("id", existentUuid, "position", Double.valueOf(existentPosition)))
        );
    }
}