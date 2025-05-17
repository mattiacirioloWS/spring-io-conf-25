package net.springio.conference.attendee.infrastructure.persistence;

import jakarta.persistence.*;
import net.springio.conference.attendee.domain.AttendeeId;

@Entity
@Table(name = "attendees")
public class AttendeeEntity {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    private AttendeeId id;
    private String firstName;
    private String lastName;

    public AttendeeId getId() {
        return id;
    }

    public void setId(AttendeeId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
