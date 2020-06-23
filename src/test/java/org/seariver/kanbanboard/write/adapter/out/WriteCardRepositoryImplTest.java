package org.seariver.kanbanboard.write.adapter.out;

import helper.DataSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.write.domain.core.Card;
import org.seariver.kanbanboard.write.domain.core.WriteCardRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class WriteCardRepositoryImplTest extends DataSourceHelper {

    private WriteCardRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new WriteCardRepositoryImpl(dataSource);
    }

    @Test
    void MUST_ImplementInterface() {
        assertThat(repository).isInstanceOf(WriteCardRepository.class);
    }

    @Test
    void WHEN_CreatingCard_GIVEN_ValidData_MUST_PersistOnDatabase() {

        // given
        var bucketId = 1;
        var uuid = UUID.randomUUID();
        var position = faker.number().randomDouble(3, 1, 10);
        var name = faker.pokemon().name();
        var expected = new Card()
            .setBucketId(bucketId)
            .setUuid(uuid)
            .setPosition(position)
            .setName(name);

        // when
        repository.create(expected);

        // then
        var actualOptional = repository.findByUuid(uuid);
        Card actual = actualOptional.get();
        assertThat(actual.getBucketId()).isEqualTo(bucketId);
        assertThat(actual.getUuid()).isEqualTo(expected.getUuid());
        assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }
}