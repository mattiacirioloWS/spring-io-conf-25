package net.springio.conference.session.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
    List<SessionEntity> findByTitleTextContainingIgnoreCase(String title);
}
