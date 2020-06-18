package org.seariver.kanbanboard.write.domain.application;

public interface Handler<T extends Command> {

    void handle(T command);
}
