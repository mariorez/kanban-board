package org.seariver.kanbanboard.write.observable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seariver.kanbanboard.write.domain.application.Command;
import org.seariver.kanbanboard.write.domain.exception.DomainException;

import java.util.HashMap;
import java.util.Map;

public class CommandEvent {

    private final Command command;
    private Exception exception;
    private long startTime;
    private long stopTime;

    public CommandEvent(Object source, Command command) {
        startTimer();
        this.command = command;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        stopTime = System.nanoTime();
    }

    public long getElapsedTimeInNano() {
        return stopTime - startTime;
    }

    public long getElapsedTimeInMilli() {
        return getElapsedTimeInNano() / 1_000_000L;
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
        message.put("elapsedTimeInMilli", getElapsedTimeInMilli());

        try {

            if (hasError()) {
                message.put("message", exception.getMessage());

                if (exception instanceof DomainException domainException && domainException.hasError()) {
                    message.put("errors", domainException.getErrors().toString());
                }
            }

            return mapper.writeValueAsString(message);

        } catch (JsonProcessingException jsonException) {
            return String.format("%s - %s", command, jsonException);
        }
    }
}
