package net.springio.conference.session.config;

import net.springio.conference.session.application.FindSessions;
import net.springio.conference.session.domain.SessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public FindSessions findSessions(SessionRepository sessionRepository) {
        return new FindSessions(sessionRepository);
    }
}
