package net.springio.conference.order.domain.event;

import net.springio.conference.shared.domain.Event;
import net.springio.conference.shared.domain.Price;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record OrderItemPriceUpdatedEvent(UUID orderId, UUID orderItemId, Price price) implements Event {
    public OrderItemPriceUpdatedEvent {
        if (orderId == null || orderItemId == null || price == null) {
            throw new IllegalArgumentException("Order ID, Order item ID, and Price cannot be null");
        }
    }
}
