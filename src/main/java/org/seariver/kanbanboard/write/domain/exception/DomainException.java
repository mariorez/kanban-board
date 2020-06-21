package org.seariver.kanbanboard.write.domain.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class DomainException extends RuntimeException {

    public enum Error {

        INVALID_DUPLICATED_DATA("Invalid duplicated data"),
        BUCKET_NOT_EXIST("Bucket not exist");

        private String message;

        Error(String message) {
            this.message = message;
        }
    }

    private final Map<String, Object> errors = new HashMap<>();

    public DomainException(Error error) {
        super(error.message);
    }

    public DomainException(Error error, Throwable cause) {
        super(error.message, cause);
    }

    public void addError(String key, Object value) {
        errors.put(key, value);
    }

    public void addError(String key, int value) {
        errors.put(key, Integer.valueOf(value));
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }
}
