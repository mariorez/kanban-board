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

import java.time.LocalDateTime;
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
    void WHEN_CreatingBucket_GIVEN_ValidData_MUST_PersistOnDatabase(UUID id,
                                                                    double position,
                                                                    String name) {
        // given
        var expected = new Bucket()
            .setExternalId(id)
            .setPosition(position)
            .setName(name);

        // when
        repository.create(expected);

        // then
        var actualOptional = repository.findByExteranlId(id);
        var actual = actualOptional.get();
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(actual.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    void WHEN_CreatingBucket_GIVEN_AlreadyExistentKey_MUST_ThrowException(UUID id,
                                                                          double position,
                                                                          Map<String, Object> expectedError) {
        // given
        var expected = new Bucket()
            .setExternalId(id)
            .setPosition(position)
            .setName("WHATEVER");

        // then
        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class, () -> repository.create(expected));

        // when
        assertThat(exception.getMessage()).isEqualTo("Invalid duplicated data");
        assertThat(exception.getCode()).isEqualTo(1000);
        assertThat(exception.getErrors()).containsExactlyInAnyOrderEntriesOf(expectedError);
    }

    @Test
    void WHEN_UpdatingBucket_WITH_ValidData_MUST_SaveOnDatabase() {

        // given
        var id = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
        var actualBucket = repository.findByExteranlId(id).get();
        assertThat(actualBucket.getPosition()).isEqualTo(200.987);
        assertThat(actualBucket.getName()).isEqualTo("SECOND-BUCKET");

        var position = faker.number().randomDouble(3, 1, 10);
        var name = faker.pokemon().name();
        actualBucket.setPosition(position).setName(name);

        // when
        repository.update(actualBucket);

        // then
        var expectedBucket = repository.findByExteranlId(id).get();
        assertThat(expectedBucket.getExternalId()).isEqualTo(id);
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

        var existentExternalId = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
        var existentPositionSameRegister = 200.987;
        var existentPositionAnotherRegister = 100.15;
        var validPosition = faker.number().randomDouble(3, 1, 10);

        return Stream.of(
            arguments(existentExternalId, validPosition, Map.of("id", existentExternalId)),
            arguments(UUID.randomUUID(), existentPositionSameRegister, Map.of("position", existentPositionSameRegister)),
            arguments(existentExternalId, existentPositionSameRegister, Map.of("id", existentExternalId, "position", existentPositionSameRegister)),
            arguments(existentExternalId, existentPositionAnotherRegister, Map.of("id", existentExternalId, "position", existentPositionAnotherRegister))
        );
    }
}