package org.seariver.kanbanboard.write.domain.core;

import helper.TestHelper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class BucketTest extends TestHelper {

    @Test
    void GIVEN_ValidData_MUST_RetrieveSameData() {

        // given
        var id = 1;
        var uuid = UUID.randomUUID();
        var position = faker.number().randomDouble(3, 1, 10);
        var name = faker.pokemon().name();
        var createdAt = LocalDateTime.now();
        var updatedAt = LocalDateTime.now();

        // when
        var bucket = new Bucket()
            .setId(id)
            .setUuid(uuid)
            .setPosition(position)
            .setName(name)
            .setCreatedAt(createdAt)
            .setUpdatedAt(updatedAt);

        // then
        assertThat(bucket.getId()).isEqualTo(id);
        assertThat(bucket.getUuid()).isEqualTo(uuid);
        assertThat(bucket.getPosition()).isEqualTo(position);
        assertThat(bucket.getName()).isEqualTo(name);
        assertThat(bucket.getCreatedAt()).isEqualTo(createdAt);
        assertThat(bucket.getUpdatedAt()).isEqualTo(updatedAt);
    }
}
