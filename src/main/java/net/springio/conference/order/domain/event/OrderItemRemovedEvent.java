package net.springio.conference.order.domain.event;

import net.springio.conference.shared.domain.Event;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record OrderItemRemovedEvent(UUID orderId, UUID itemId) implements Event {
    public OrderItemRemovedEvent {
        if (orderId == null || itemId == null) {
            throw new IllegalArgumentException("Order ID, and Item ID cannot be null");
        }
    }
}
