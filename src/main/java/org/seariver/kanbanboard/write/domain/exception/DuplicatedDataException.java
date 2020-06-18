package org.seariver.kanbanboard.write.domain.exception;

import java.util.HashMap;
import java.util.Map;

public class DuplicatedDataException extends DomainException {

    private Map<String, Object> errors = new HashMap<>();

    public DuplicatedDataException(String message, Throwable throwable) {
        super(message, throwable);
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
}
