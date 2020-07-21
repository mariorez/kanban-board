package org.seariver.kanbanboard.read.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CardDto(
    @JsonProperty("id") UUID externalId,
    @JsonProperty("position") double position,
    @JsonProperty("name") String name) {
}
