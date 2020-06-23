package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.UUID;

public record CardDto(
    @JsonProperty("id")UUID id,
    @JsonProperty("bucketId")UUID bucketId,
    @Positive @JsonProperty("position")double position,
    @NotBlank @Size(min = 1, max = 100) @JsonProperty("name")String name) {
}
