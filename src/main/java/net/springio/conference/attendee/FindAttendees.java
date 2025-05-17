package net.springio.conference.attendee;

import net.springio.conference.attendee.application.AttendeeDto;
import org.jmolecules.architecture.layered.InterfaceLayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@InterfaceLayer
public interface FindAttendees {
    List<AttendeeDto> all();

    Optional<AttendeeDto> byId(UUID id);
}
