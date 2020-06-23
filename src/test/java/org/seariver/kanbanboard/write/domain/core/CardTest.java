package org.seariver.kanbanboard.write.domain.core;

import helper.TestHelper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class CardTest extends TestHelper {

    @Test
    void GIVEN_ValidData_MUST_RetrieveSameData() {

        // given
        var id = 1;
        var bucketId = 2;
        var uuid = UUID.randomUUID();
        var position = faker.number().randomDouble(3, 1, 10);
        var name = faker.pokemon().name();
        var createdAt = LocalDateTime.now();
        var updatedAt = LocalDateTime.now();

        // when
        var card = new Card()
            .setId(id)
            .setBucketId(bucketId)
            .setUuid(uuid)
            .setPosition(position)
            .setName(name)
            .setCreatedAt(createdAt)
            .setUpdatedAt(updatedAt);

        // then
        assertThat(card.getId()).isEqualTo(id);
        assertThat(card.getBucketId()).isEqualTo(bucketId);
        assertThat(card.getUuid()).isEqualTo(uuid);
        assertThat(card.getPosition()).isEqualTo(position);
        assertThat(card.getName()).isEqualTo(name);
        assertThat(card.getCreatedAt()).isEqualTo(createdAt);
        assertThat(card.getUpdatedAt()).isEqualTo(updatedAt);
    }
}
