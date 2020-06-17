package org.seariver.kanbanboard.write.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicatedDataExceptionTest {

    @Test
    void DuplicatedDataException_MUST_Extends_DomainException() {

        // given
        var exception = new DuplicatedDataException("Test", new RuntimeException());

        // then
        assertThat(exception).isInstanceOf(DomainException.class);
    }
}