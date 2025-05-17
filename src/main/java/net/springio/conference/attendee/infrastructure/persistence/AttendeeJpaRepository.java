package net.springio.conference.attendee.infrastructure.persistence;

import net.springio.conference.attendee.domain.AttendeeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeJpaRepository extends JpaRepository<AttendeeEntity, AttendeeId> {

}
