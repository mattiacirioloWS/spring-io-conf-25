package net.springio.conference.session.infrastructure.persistence;

import net.springio.conference.session.domain.Session;
import net.springio.conference.shared.infrastructure.EventsMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EventsMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SessionMapper {
    SessionEntity fromAggregate(Session session);
}
