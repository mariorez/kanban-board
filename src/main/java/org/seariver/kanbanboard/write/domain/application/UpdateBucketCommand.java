package org.seariver.kanbanboard.write.domain.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UpdateBucketCommand(@JsonProperty("id")UUID id,
                                  @JsonProperty("position")double position,
                                  @JsonProperty("name")String name) implements Command {
}
