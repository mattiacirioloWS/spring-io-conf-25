package net.springio.conference.attendee.domain;

import java.io.Serializable;
import java.util.UUID;

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
