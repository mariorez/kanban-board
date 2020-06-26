package org.seariver.kanbanboard.read.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BucketDto {

    @JsonProperty("id")
    private UUID uuid;
    @JsonProperty("position")
    private double position;
    @JsonProperty("name")
    private String name;
    @JsonProperty("cards")
    private final List<CardDto> cards = new ArrayList<>();

    public UUID getUuid() {
        return uuid;
    }

    public BucketDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public double getPosition() {
        return position;
    }

    public BucketDto setPosition(double position) {
        this.position = position;
        return this;
    }

    public String getName() {
        return name;
    }

    public BucketDto setName(String name) {
        this.name = name;
        return this;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public BucketDto addCard(CardDto card) {
        cards.add(card);
        return this;
    }
}
