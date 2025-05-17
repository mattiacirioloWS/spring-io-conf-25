package net.springio.conference.attendee.presentation.api;

import net.springio.conference.attendee.FindAttendees;
import net.springio.conference.attendee.application.AttendeeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendees")
public class AttendeeController {
    private final FindAttendees findAttendees;

    public AttendeeController(FindAttendees findAttendees) {
        this.findAttendees = findAttendees;
    }

    @GetMapping
    public List<AttendeeDto> findAll() {
        return findAttendees.all();
    }

    @GetMapping("/{attendeeId}")
    public AttendeeDto findById(@PathVariable UUID attendeeId) {
        return findAttendees.byId(attendeeId)
                .orElseThrow(() -> new IllegalArgumentException("Attendee not found: " + attendeeId));
    }
}
