package org.seariver.kanbanboard.write.domain.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

public record CreateBucketCommand(
    @NotNull @JsonProperty("id") UUID id,
    @Positive @JsonProperty("position") double position,
    @NotBlank @JsonProperty("name") String name) implements Command {
}
