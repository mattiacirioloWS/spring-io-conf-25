package net.springio.conference.order.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ItemName(String name) {

    public ItemName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
