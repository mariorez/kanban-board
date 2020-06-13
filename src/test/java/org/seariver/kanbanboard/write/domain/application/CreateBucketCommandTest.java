package org.seariver.kanbanboard.write.domain.application;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBucketCommandTest {

    @Test
    void GIVEN_ValidData_MUST_RetrieveSameData() {

        // given
        UUID id = UUID.randomUUID();
        int position = 1;
        String name = "TODO";

        // when
        CreateBucketCommand command = new CreateBucketCommand(id, position, name);

        // then
        assertThat(command.id()).isEqualTo(id);
        assertThat(command.position()).isEqualTo(position);
        assertThat(command.name()).isEqualTo(name);
    }
}
