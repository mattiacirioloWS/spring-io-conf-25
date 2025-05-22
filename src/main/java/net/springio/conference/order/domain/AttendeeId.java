package net.springio.conference.order.domain;

import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;
import java.util.UUID;

@ValueObject
public record AttendeeId(UUID uuid) implements Serializable {
    public AttendeeId {
        if (uuid == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
