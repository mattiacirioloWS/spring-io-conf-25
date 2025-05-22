package net.springio.conference.session.infrastructure.persistence;

import net.springio.conference.session.domain.Session;
import net.springio.conference.session.domain.SessionFactory;
import net.springio.conference.session.domain.SessionId;
import net.springio.conference.session.domain.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class SessionRepositoryImpl implements SessionRepository {
    private final SessionJpaRepository sessionJpaRepository;
    private final SessionMapper sessionMapper;

    public SessionRepositoryImpl(SessionJpaRepository sessionJpaRepository, SessionMapper sessionMapper) {
        this.sessionJpaRepository = sessionJpaRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public Stream<Session> findAll() {
        return sessionJpaRepository.findAll()
                .stream()
                .map(this::toAggregate);
    }

    @Override
    public Stream<Session> findByTitleContainingIgnoreCase(String title) {
        return sessionJpaRepository.findByTitleTextContainingIgnoreCase(title)
                .stream()
                .map(this::toAggregate);
    }

    @Override
    public Optional<Session> findById(SessionId id) {
        return sessionJpaRepository.findById(id)
                .map(this::toAggregate);
    }

    @Override
    public Session save(Session session) {
        SessionEntity savedSession = sessionJpaRepository.save(sessionMapper.fromAggregate(session));
        return toAggregate(savedSession);
    }

    private Session toAggregate(SessionEntity session) {
        return SessionFactory.reconstitute(
                session.getId(),
                session.getTitle(),
                session.getSpeakers(),
                session.getPrice()
        );
    }
}
