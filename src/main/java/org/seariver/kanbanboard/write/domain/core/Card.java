package org.seariver.kanbanboard.write.domain.core;

import java.time.LocalDateTime;
import java.util.UUID;

public class Card {

    private Long id;
    private Long bucketId;
    private UUID uuid;
    private double position;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public Card setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBucketId() {
        return bucketId;
    }

    public Card setBucketId(Long bucketId) {
        this.bucketId = bucketId;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Card setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public double getPosition() {
        return position;
    }

    public Card setPosition(double position) {
        this.position = position;
        return this;
    }

    public String getName() {
        return name;
    }

    public Card setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Card setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Card setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
