package net.springio.conference.session.infrastructure.persistence;

import net.springio.conference.session.domain.Session;
import net.springio.conference.session.domain.SessionRepository;
import org.springframework.stereotype.Repository;

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
        return sessionJpaRepository.findAll().stream()
                .map(sessionMapper::toAggregate);
    }

    @Override
    public Stream<Session> findByTitleContainingIgnoreCase(String title) {
        return sessionJpaRepository.findByTitleTextContainingIgnoreCase(title)
                .stream()
                .map(sessionMapper::toAggregate);
    }
}