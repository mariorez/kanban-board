package org.seariver.kanbanboard.read.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BucketDto {

    @JsonProperty("id")
    private final UUID uuid;
    @JsonProperty("position")
    private final double position;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("cards")
    private final List<CardDto> cards = new ArrayList<>();

    public BucketDto(UUID uuid, double position, String name) {
        this.uuid = uuid;
        this.position = position;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
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
