package net.springio.conference.session.application;

import net.springio.conference.session.FindSessions;
import net.springio.conference.session.SessionDto;
import net.springio.conference.session.domain.Session;
import net.springio.conference.session.domain.SessionId;
import net.springio.conference.session.domain.SessionRepository;
import net.springio.conference.shared.application.UseCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UseCase
public class FindSessionsImpl implements FindSessions {
    private final SessionRepository sessionRepository;

    public FindSessionsImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Optional<SessionDto> findById(UUID id) {
        return sessionRepository.findById(new SessionId(id))
                .map(this::mapToDto);
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
