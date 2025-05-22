package net.springio.conference.order.domain.event;

import net.springio.conference.shared.domain.Event;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record OrderItemAddedEvent(UUID orderId, UUID itemId) implements Event {
    public OrderItemAddedEvent {
        if (orderId == null || itemId == null) {
            throw new IllegalArgumentException("Order ID, and Item ID cannot be null");
        }
    }
}
