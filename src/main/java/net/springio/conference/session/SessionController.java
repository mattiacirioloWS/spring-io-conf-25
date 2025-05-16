package net.springio.conference.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
class SessionController {
    private final SessionService sessionService;

    SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<SessionEntity> findAll() {
        return sessionService.findAll();
    }

    @GetMapping("/search/{title}")
    public List<SessionEntity> findByTitle(@PathVariable String title) {
        return sessionService.findByTitle(title);
    }
}
