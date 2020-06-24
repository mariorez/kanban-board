package org.seariver.kanbanboard.write.domain.application;

import java.util.UUID;

public record CreateBucketCommand(
    UUID uuid,
    double position,
    String name) implements Command {
}
