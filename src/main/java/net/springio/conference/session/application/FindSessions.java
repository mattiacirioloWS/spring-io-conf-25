package net.springio.conference.session.application;

import net.springio.conference.session.domain.Session;
import net.springio.conference.session.domain.SessionRepository;
import net.springio.conference.shared.application.UseCase;

import java.util.List;

@UseCase
public class FindSessions {
    private final SessionRepository sessionRepository;

    public FindSessions(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<SessionDto> findByTitle(String title) {
        return sessionRepository.findByTitleContainingIgnoreCase(title)
                .map(this::mapToDto)
                .toList();
    }

    public List<SessionDto> findAll() {
        return sessionRepository.findAll()
                .map(this::mapToDto)
                .toList();
    }

    private SessionDto mapToDto(Session session) {
        return new SessionDto(session.getId().uuid(),
                session.getTitle().text(),
                session.getSpeakers().names(),
                session.getPrice().amount());
    }

}
