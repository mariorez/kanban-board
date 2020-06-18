package org.seariver.kanbanboard.write.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.seariver.kanbanboard.write.domain.exception.DomainException.Error.INVALID_DUPLICATED_DATA;

class DuplicatedDataExceptionTest {

    @Test
    void DuplicatedDataException_MUST_Extends_DomainException() {

        // given
        var exception = new DuplicatedDataException(INVALID_DUPLICATED_DATA, new RuntimeException());

        // then
        assertThat(exception).isInstanceOf(DomainException.class);
    }
}