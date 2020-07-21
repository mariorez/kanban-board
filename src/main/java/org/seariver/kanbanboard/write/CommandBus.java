package org.seariver.kanbanboard.write;

import org.seariver.kanbanboard.write.domain.application.Command;
import org.seariver.kanbanboard.write.domain.application.Handler;
import org.seariver.kanbanboard.write.observable.CommandEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CommandBus {

    private ApplicationContext context;
    private ApplicationEventPublisher publisher;

    public CommandBus(ApplicationContext context, ApplicationEventPublisher publisher) {
        this.context = context;
        this.publisher = publisher;
    }

    public void execute(Command command) {

        var event = new CommandEvent(this, command);

        try {
            handle(command);
        } catch (Exception exception) {
            event.setException(exception);
            throw exception;
        } finally {
            publisher.publishEvent(event);
        }
    }

    private void handle(Command command) {
        var handlerName = command.getClass().getSimpleName().replace("Command", "Handler");
        var handlerBeanName = Character.toLowerCase(handlerName.charAt(0)) + handlerName.substring(1);
        Handler<Command> handler = (Handler) context.getBean(handlerBeanName);
        handler.handle(command);
    }
}
