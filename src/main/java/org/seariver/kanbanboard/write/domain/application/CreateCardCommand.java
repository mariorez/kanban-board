package org.seariver.kanbanboard.write.domain.application;

import java.util.UUID;

public record CreateCardCommand(
    UUID id,
    UUID bucketId,
    double position,
    String name) {
}
