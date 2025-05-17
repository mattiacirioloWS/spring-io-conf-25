package net.springio.conference.attendee.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@AggregateRoot
public class Attendee {
    @Identity
    private final AttendeeId id;
    private final String firstName;
    private final String lastName;

    Attendee(AttendeeId id, String firstName, String lastName) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AttendeeId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
