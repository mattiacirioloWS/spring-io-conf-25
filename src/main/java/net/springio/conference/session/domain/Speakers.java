package net.springio.conference.session.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Speakers(String names) {
    public Speakers {
        if (names == null || names.isBlank()) {
            throw new IllegalArgumentException("Names cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return names;
    }
}
