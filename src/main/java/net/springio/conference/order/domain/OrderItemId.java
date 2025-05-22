package net.springio.conference.order.domain;

import java.io.Serializable;
import java.util.UUID;

public record OrderItemId(UUID uuid) implements Serializable {
    public OrderItemId {
        if (uuid == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    public static OrderItemId create() {
        return new OrderItemId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
