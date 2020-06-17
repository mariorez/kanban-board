package org.seariver.kanbanboard.write.domain.exception;

public class DuplicatedDataException extends RuntimeException {

    public DuplicatedDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
