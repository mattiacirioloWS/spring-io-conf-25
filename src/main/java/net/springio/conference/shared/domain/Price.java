package net.springio.conference.shared.domain;

import java.math.BigDecimal;

public record Price(BigDecimal amount) {
    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
    }

    public static Price ZERO = new Price(BigDecimal.ZERO);

    public Price add(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        return new Price(this.amount.add(price.amount));
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
