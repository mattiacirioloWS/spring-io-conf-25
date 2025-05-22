package net.springio.conference.shared.infrastructure;

import net.springio.conference.shared.domain.DomainEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.List;

public abstract class EntityWithEvents<E extends AbstractAggregateRoot<E>> extends AbstractAggregateRoot<E> {
    void setEvents(List<DomainEvent> events) {
        if (events != null && !events.isEmpty()) {
            events.forEach(this::registerEvent);
        }
    }
}
