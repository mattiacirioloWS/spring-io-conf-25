package net.springio.conference.attendee.application;

import java.util.UUID;

public class AttendeeDto {
    private final UUID id;
    private final String firstName;
    private final String lastName;

    AttendeeDto(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
