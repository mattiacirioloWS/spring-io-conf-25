package net.springio.conference.session.infrastructure.persistence;

import net.springio.conference.session.domain.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    SessionEntity fromAggregate(Session session);

    default Session toAggregate(SessionEntity sessionEntity) {
        return Session.Builder.reconstitute(
                sessionEntity.getId(),
                sessionEntity.getTitle(),
                sessionEntity.getSpeakers(),
                sessionEntity.getPrice()
        );
    }
}
