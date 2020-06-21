package org.seariver.kanbanboard.write;

import org.seariver.kanbanboard.write.domain.application.Command;
import org.seariver.kanbanboard.write.domain.application.Handler;
import org.seariver.kanbanboard.write.event.CommandEvent;
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

        CommandEvent event = new CommandEvent(this, command);

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
        String handlerName = String.format("%sHandler", command.getClass().getSimpleName());
        String handlerBeanName = Character.toLowerCase(handlerName.charAt(0)) + handlerName.substring(1);
        Handler<Command> handler = (Handler) context.getBean(handlerBeanName);
        handler.handle(command);
    }
}
