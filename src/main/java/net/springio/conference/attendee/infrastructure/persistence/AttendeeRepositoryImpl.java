package net.springio.conference.attendee.infrastructure.persistence;

import net.springio.conference.attendee.domain.Attendee;
import net.springio.conference.attendee.domain.AttendeeFactory;
import net.springio.conference.attendee.domain.AttendeeId;
import net.springio.conference.attendee.domain.AttendeeRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class AttendeeRepositoryImpl implements AttendeeRepository {
    private final AttendeeJpaRepository attendeeJpaRepository;

    public AttendeeRepositoryImpl(AttendeeJpaRepository attendeeJpaRepository) {
        this.attendeeJpaRepository = attendeeJpaRepository;
    }

    @Override
    public Optional<Attendee> findById(AttendeeId id) {
        return attendeeJpaRepository.findById(id)
                .map(this::toAggregate);
    }

    @Override
    public Stream<Attendee> findAll() {
        return attendeeJpaRepository.findAll().stream()
                .map(this::toAggregate);
    }

    private Attendee toAggregate(AttendeeEntity attendeeEntity) {
        return AttendeeFactory.reconstitute(
                attendeeEntity.getId(),
                attendeeEntity.getFirstName(),
                attendeeEntity.getLastName()
        );
    }
}
