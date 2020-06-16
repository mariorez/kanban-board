package org.seariver.kanbanboard.write.domain.exception;

public class DuplicatedDataException extends RuntimeException {

    public DuplicatedDataException(String message, Throwable throwable) {
        super(message, throwable);
    }

    @Override
    public String getMessage() {

        var fieldName = "";
        var message = super.getMessage();

        if (message.contains("BUCKET(UUID)")) {
            fieldName = "id";
        }

        if (message.contains("BUCKET(POSITION)")) {
            fieldName = "position";
        }

        return "Invalid duplicated data: " + fieldName;
    }
}
