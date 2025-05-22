package net.springio.conference.session.presentation.api;

import net.springio.conference.session.SessionDto;
import net.springio.conference.session.application.ChangePrice;
import net.springio.conference.session.application.FindSessionsImpl;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
class SessionController {
    private final FindSessionsImpl findSessions;
    private final ChangePrice changePrice;

    SessionController(FindSessionsImpl findSessions, ChangePrice changePrice) {
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
