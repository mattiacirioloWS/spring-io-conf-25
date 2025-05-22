package net.springio.conference.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRootWithDomainEvents {
    private final List<Event> events = new ArrayList<>();

    protected final <T extends Event> void registerEvent(T event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    protected final <T extends Event> void registerEvents(List<T> events) {
        if (!(events == null || events.isEmpty())) {
            this.events.addAll(events);
        }
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
