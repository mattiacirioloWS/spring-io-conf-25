package net.springio.conference.session.presentation.api;

import net.springio.conference.session.application.FindSessions;
import net.springio.conference.session.application.SessionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
class SessionController {
    private final FindSessions findSessions;

    SessionController(FindSessions findSessions) {
        this.findSessions = findSessions;
    }

    @GetMapping
    public List<SessionDto> findAll() {
        return findSessions.findAll();
    }

    @GetMapping("/search/{title}")
    public List<SessionDto> findByTitle(@PathVariable String title) {
        return findSessions.findByTitle(title);
    }
}
