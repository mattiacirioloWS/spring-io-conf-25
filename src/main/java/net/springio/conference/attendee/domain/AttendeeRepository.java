package net.springio.conference.attendee.domain;

import java.util.Optional;
import java.util.stream.Stream;

public interface AttendeeRepository {
    Optional<Attendee> findById(AttendeeId id);

    Stream<Attendee> findAll();
}
