package net.springio.conference.attendee.config;

import net.springio.conference.attendee.application.FindAttendeeImpl;
import net.springio.conference.attendee.domain.AttendeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttendeeConfig {
    @Bean
    public FindAttendeeImpl findAttendee(AttendeeRepository attendeeRepository) {
        return new FindAttendeeImpl(attendeeRepository);
    }
}
