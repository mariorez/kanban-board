package org.seariver.kanbanboard.read.adapter.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record BucketDto(@JsonProperty("id") UUID uuid,
                        @JsonProperty("position") double position,
                        @JsonProperty("name") String name) {
}
