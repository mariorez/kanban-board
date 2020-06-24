package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.UUID;

public record BucketDto(
    @JsonProperty("id")UUID uuid,
    @Positive @JsonProperty("position")double position,
    @NotBlank @Size(min = 1, max = 100) @JsonProperty("name")String name) {
}
