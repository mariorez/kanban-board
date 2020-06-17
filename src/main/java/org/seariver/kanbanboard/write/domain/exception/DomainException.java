package org.seariver.kanbanboard.write.domain.exception;

public abstract class DomainException extends RuntimeException {

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
