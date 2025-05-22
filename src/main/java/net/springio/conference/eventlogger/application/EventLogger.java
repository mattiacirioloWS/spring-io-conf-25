package net.springio.conference.eventlogger.application;

import net.springio.conference.session.domain.event.PriceChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EventLogger {

    Logger log = Logger.getLogger(EventLogger.class.getName());

    @EventListener // this will be emitted and handled within the same transaction (before the transaction commits)
    public void logEvent(PriceChangedEvent event) {
        log.info("Event occurred: " + event);
    }

    //@ApplicationModuleListener // Async TransactionalEventListener with AFTER_COMMIT phase (the transaction commits)
    public void logModuleEvent(PriceChangedEvent event) {
        log.info("Module event occurred: " + event);
    }

    //@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT) // sync with before commit
    public void logTransactionalEventBefore(PriceChangedEvent event) {
        log.info("Before commit with transactional event occurred: " + event);
    }

    //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // sync with after commit
    public void logTransactionalEvent(PriceChangedEvent event) {
        log.info("Transactional event occurred: " + event);
    }
}
