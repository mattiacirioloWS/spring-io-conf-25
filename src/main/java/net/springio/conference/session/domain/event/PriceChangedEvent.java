package net.springio.conference.session.domain.event;

import net.springio.conference.shared.domain.Event;
import net.springio.conference.shared.domain.Price;

import java.util.UUID;


public record PriceChangedEvent(UUID session, Price newPrice) implements Event {
    public PriceChangedEvent {
        if (session == null || newPrice == null) {
            throw new IllegalArgumentException("Session ID and new price cannot be null");
        }
    }
}
