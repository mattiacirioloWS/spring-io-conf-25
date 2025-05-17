package net.springio.conference.attendee.domain;

import org.jmolecules.ddd.annotation.Factory;

@Factory
public final class AttendeeFactory {
    public static Attendee reconstitute(AttendeeId id, String firstName, String lastName) {
        return new Attendee(id, firstName, lastName);
    }
}
