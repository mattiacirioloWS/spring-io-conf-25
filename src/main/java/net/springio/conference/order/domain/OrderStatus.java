package net.springio.conference.order.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public final class OrderStatus {
    private final Status status;

    private OrderStatus(Status status) {
        this.status = status;
    }

    public static OrderStatus pending() {
        return new OrderStatus(Status.PENDING);
    }

    public static OrderStatus completed() {
        return new OrderStatus(Status.COMPLETED);
    }

    static OrderStatus cancelled() {
        return new OrderStatus(Status.CANCELLED);
    }

    public static OrderStatus from(String status) {
        return new OrderStatus(Status.valueOf(status));
    }

    public String getName() {
        return this.status.name();
    }

    public Boolean isPending() {
        return this.status == Status.PENDING;
    }

    private enum Status {
        PENDING,
        COMPLETED,
        CANCELLED
    }


}
