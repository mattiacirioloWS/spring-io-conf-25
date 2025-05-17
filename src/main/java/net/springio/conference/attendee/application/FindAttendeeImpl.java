package net.springio.conference.attendee.application;

import net.springio.conference.attendee.FindAttendees;
import net.springio.conference.attendee.domain.Attendee;
import net.springio.conference.attendee.domain.AttendeeId;
import net.springio.conference.attendee.domain.AttendeeRepository;
import net.springio.conference.shared.application.UseCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UseCase
public class FindAttendeeImpl implements FindAttendees {
    private final AttendeeRepository attendeeRepository;

    public FindAttendeeImpl(AttendeeRepository attendeeRepository) {
        this.attendeeRepository = attendeeRepository;
    }

    @Override
    public List<AttendeeDto> all() {
        return attendeeRepository.findAll()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public Optional<AttendeeDto> byId(UUID id) {
        return attendeeRepository.findById(new AttendeeId(id))
                .map(this::mapToDto);
    }

    private AttendeeDto mapToDto(Attendee attendee) {
        return new AttendeeDto(
                attendee.getId().uuid(),
                attendee.getFirstName(),
                attendee.getLastName()
        );
    }
}
