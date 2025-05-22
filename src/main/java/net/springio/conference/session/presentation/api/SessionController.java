package net.springio.conference.session.presentation.api;

import net.springio.conference.session.application.ChangePrice;
import net.springio.conference.session.application.FindSessions;
import net.springio.conference.session.application.SessionDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
class SessionController {
    private final FindSessions findSessions;
    private final ChangePrice changePrice;

    SessionController(FindSessions findSessions, ChangePrice changePrice) {
        this.findSessions = findSessions;
        this.changePrice = changePrice;
    }

    @GetMapping
    public List<SessionDto> findAll() {
        return findSessions.findAll();
    }

    @GetMapping("/search/{title}")
    public List<SessionDto> findByTitle(@PathVariable String title) {
        return findSessions.findByTitle(title);
    }

    @PostMapping("/{sessionId}/price/{newPrice}")
    public void changePrice(@PathVariable UUID sessionId, @PathVariable BigDecimal newPrice) {
        changePrice.execute(sessionId, newPrice);
    }

}
