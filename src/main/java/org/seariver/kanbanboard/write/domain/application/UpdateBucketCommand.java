package org.seariver.kanbanboard.write.domain.application;

import java.util.UUID;

public record UpdateBucketCommand(
    UUID id,
    double position,
    String name) implements Command {
}
