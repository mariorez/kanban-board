package org.seariver.kanbanboard.write.domain.exception;

public class DuplicatedDataException extends DomainException {

    public DuplicatedDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
