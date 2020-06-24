package org.seariver.kanbanboard.read.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record BucketDto(
    @JsonProperty("id")UUID uuid,
    @JsonProperty("position")double position,
    @JsonProperty("name")String name,
    @JsonProperty("cards")List<CardDto>cards) {
}
