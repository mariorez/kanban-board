package org.seariver.kanbanboard.write.domain.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class DomainException extends RuntimeException {

    private Map<String, Object> errors = new HashMap<>();

    public DomainException(String message, Throwable cause) {
        super(message, cause);
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
