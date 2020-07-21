package org.seariver.kanbanboard.read.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BucketDto {

    @JsonProperty("id")
    private final UUID externalId;
    @JsonProperty("position")
    private final double position;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("cards")
    private final List<CardDto> cards = new ArrayList<>();

    public BucketDto(UUID externalId, double position, String name) {
        this.externalId = externalId;
        this.position = position;
        this.name = name;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public double getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void addCard(CardDto cardDto) {
        cards.add(cardDto);
    }
}
