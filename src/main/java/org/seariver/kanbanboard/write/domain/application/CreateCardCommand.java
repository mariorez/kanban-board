package org.seariver.kanbanboard.write.domain.application;

import java.util.UUID;

public record CreateCardCommand(
    UUID externalId,
    UUID bucketId,
    double position,
    String name) implements Command {
}
