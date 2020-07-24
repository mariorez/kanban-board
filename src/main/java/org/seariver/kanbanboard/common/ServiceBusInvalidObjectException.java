package org.seariver.kanbanboard.common;

public class ServiceBusInvalidObjectException extends RuntimeException {

    public ServiceBusInvalidObjectException(Event event) {
        super(String.format("ServiceBus does not recognizes Object of type: %s",
                event.getSource().getClass().getCanonicalName()));
    }
}
