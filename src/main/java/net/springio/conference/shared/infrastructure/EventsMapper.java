package net.springio.conference.shared.infrastructure;

import net.springio.conference.shared.domain.AggregateRootWithDomainEvents;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventsMapper {

    @AfterMapping
    default void mapEvents(AggregateRootWithDomainEvents aggregate, @MappingTarget EntityWithEvents<?> entityWithEvents) {
        entityWithEvents.setEvents(aggregate.getEvents());
    }
}
