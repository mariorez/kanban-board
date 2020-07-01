package org.seariver.kanbanboard.write.domain.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class DomainException extends RuntimeException {

    private static final long serialVersionUID = -7119093928181516799L;
    private int code;

    public enum Error {

        INVALID_DUPLICATED_DATA("Invalid duplicated data", 1000),
        BUCKET_NOT_EXIST("Bucket not exist", 1001);

        private String message;
        private int code;

        Error(String message, int code) {
            this.message = message;
            this.code = code;
        }
    }

    private final transient Map<String, Object> errors = new HashMap<>();

    public DomainException(Error error) {
        super(error.message);
        code = error.code;
    }

    public DomainException(Error error, Throwable cause) {
        super(error.message, cause);
        code = error.code;
    }

    public int getCode() {
        return code;
    }

    public void addError(String key, Object value) {
        errors.put(key, value);
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }
}
