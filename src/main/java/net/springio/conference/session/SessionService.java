package net.springio.conference.session;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SessionService {
    private final SessionRepository sessionRepository;

    SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<SessionEntity> findByTitle(String title) {
        return sessionRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<SessionEntity> findAll() {
        return sessionRepository.findAll();
    }
}
