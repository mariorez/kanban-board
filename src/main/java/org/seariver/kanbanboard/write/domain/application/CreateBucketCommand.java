package org.seariver.kanbanboard.write.domain.application;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

public record CreateBucketCommand(
    @NotNull UUID id,
    @Positive int position,
    @NotBlank String name) {
}
