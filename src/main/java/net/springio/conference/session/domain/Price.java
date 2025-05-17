package net.springio.conference.session.domain;

import java.math.BigDecimal;

public record Price(BigDecimal amount) {
    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
