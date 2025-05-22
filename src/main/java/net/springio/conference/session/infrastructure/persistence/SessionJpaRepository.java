package net.springio.conference.session.infrastructure.persistence;

import net.springio.conference.session.domain.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, SessionId> {
    List<SessionEntity> findByTitleTextContainingIgnoreCase(String title);
}
