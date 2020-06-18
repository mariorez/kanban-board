package org.seariver.kanbanboard.write.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seariver.kanbanboard.write.domain.application.Command;
import org.seariver.kanbanboard.write.domain.exception.DomainException;

import java.util.HashMap;
import java.util.Map;

public class CommandEvent {

    private final Command command;
    private Exception exception;

    public CommandEvent(Object source, Command command) {
        this.command = command;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    boolean isSuccess() {
        return exception == null;
    }

    boolean hasError() {
        return !isSuccess();
    }

    public String toJson() {

        var mapper = new ObjectMapper();
        Map<String, Object> message = new HashMap<>(Map.of("event", command.toString()));

        try {

            if (hasError()) {
                message.put("error", exception.getMessage());

                if (exception instanceof DomainException domainException && domainException.hasError()) {
                    message.put("details", domainException.getErrors().toString());
                }
            }

            return mapper.writeValueAsString(message);

        } catch (JsonProcessingException jsonException) {
            return String.format("%s - %s", command, jsonException);
        }
    }
}
