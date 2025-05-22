package net.springio.conference.order.domain;

import java.io.Serializable;
import java.util.UUID;

public record OrderId(UUID uuid) implements Serializable {
    public OrderId {
        if (uuid == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    static OrderId create() {
        return new OrderId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
