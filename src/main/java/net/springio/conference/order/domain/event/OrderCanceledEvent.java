package net.springio.conference.order.domain.event;


import net.springio.conference.shared.domain.Event;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record OrderCanceledEvent(UUID orderId) implements Event {
    public OrderCanceledEvent {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
    }
}
