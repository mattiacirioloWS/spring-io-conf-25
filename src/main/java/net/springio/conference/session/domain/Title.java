package net.springio.conference.session.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Title(String text) {
    public Title {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return text;
    }
}
