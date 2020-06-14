package org.seariver.kanbanboard.write.domain.core;

import java.util.Objects;
import java.util.UUID;

public class Bucket {

    private int id;
    private UUID uuid;
    private int position;
    private String name;

    public int getId() {
        return id;
    }

    public Bucket setId(int id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Bucket setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public Bucket setPosition(int position) {
        this.position = position;
        return this;
    }

    public String getName() {
        return name;
    }

    public Bucket setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bucket bucket = (Bucket) o;
        return position == bucket.position &&
            uuid.equals(bucket.uuid) &&
            name.equals(bucket.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, position, name);
    }
}
