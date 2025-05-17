package net.springio.conference.session.domain;

import java.io.Serializable;
import java.util.UUID;

public record SessionId(UUID uuid) implements Serializable {
    public SessionId {
        if (uuid == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
