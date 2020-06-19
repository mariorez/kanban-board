package org.seariver.kanbanboard.read.adapter.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record BucketDto(@JsonProperty("id")UUID uuid,
                        @JsonProperty("position")int position,
                        @JsonProperty("name")String name) {
}
