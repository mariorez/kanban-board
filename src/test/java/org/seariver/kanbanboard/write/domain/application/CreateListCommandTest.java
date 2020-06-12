package org.seariver.kanbanboard.write.domain.application;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateListCommandTest {

    @Test
    void GIVEN_ValidData_MUST_RetrieveSameData() {

        // given
        UUID id = UUID.randomUUID();
        int position = 1;
        String name = "TODO";

        // when
        CreateListCommand command = new CreateListCommand(id, position, name);

        // then
        assertThat(command.getId()).isEqualTo(id);
        assertThat(command.getPosition()).isEqualTo(position);
        assertThat(command.getName()).isEqualTo(name);
    }
}
