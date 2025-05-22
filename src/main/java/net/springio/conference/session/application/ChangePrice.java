package net.springio.conference.session.application;

import net.springio.conference.session.domain.Session;
import net.springio.conference.session.domain.SessionId;
import net.springio.conference.session.domain.SessionRepository;
import net.springio.conference.shared.application.UseCase;
import net.springio.conference.shared.domain.Price;

import java.math.BigDecimal;
import java.util.UUID;

@UseCase
public class ChangePrice {
    private final SessionRepository sessionRepository;

    public ChangePrice(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void execute(UUID sessionId, BigDecimal newPrice) {
        Session session = sessionRepository.findById(new SessionId(sessionId)).orElseThrow(
                () -> new IllegalArgumentException("Session not found with id: " + sessionId)
        );
        session.changePrice(new Price(newPrice));
        sessionRepository.save(session);
    }
}
