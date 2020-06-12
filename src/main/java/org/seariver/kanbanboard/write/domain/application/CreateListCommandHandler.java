package org.seariver.kanbanboard.write.domain.application;


import org.seariver.kanbanboard.write.domain.core.ListAggregate;

public class CreateListCommandHandler {

    private final ListAggregate listAggregate;

    public CreateListCommandHandler(ListAggregate listAggregate) {
        this.listAggregate = listAggregate;
    }

    public void handle(CreateListCommand command) {
        listAggregate.create(
            command.getId(),
            command.getPosition(),
            command.getName());
    }
}
