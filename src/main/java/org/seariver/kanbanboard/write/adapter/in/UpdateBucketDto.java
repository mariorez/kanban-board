package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record UpdateBucketDto(
    @Positive
    @JsonProperty("position")
    double position,
    @NotBlank
    @Size(min = 1, max = 100)
    @JsonProperty("name")
    String name) {
}
