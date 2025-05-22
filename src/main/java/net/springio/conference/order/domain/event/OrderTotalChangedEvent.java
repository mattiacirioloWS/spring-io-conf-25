package net.springio.conference.order.domain.event;

import net.springio.conference.shared.domain.Event;
import net.springio.conference.shared.domain.Price;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record OrderTotalChangedEvent(UUID orderId, Price total) implements Event {
    public OrderTotalChangedEvent {
        if (orderId == null || total == null) {
            throw new IllegalArgumentException("Order ID,  and Total cannot be null");
        }
    }
}
