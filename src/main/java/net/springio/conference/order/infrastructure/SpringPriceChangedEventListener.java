package net.springio.conference.order.infrastructure;

import jakarta.transaction.Transactional;
import net.springio.conference.order.domain.port.PriceChangePolicyPort;
import net.springio.conference.session.domain.event.PriceChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringPriceChangedEventListener {

    private final PriceChangePolicyPort useCase;

    public SpringPriceChangedEventListener(PriceChangePolicyPort useCase) {
        this.useCase = useCase;
    }

    @EventListener
    @Transactional
    public void on(PriceChangedEvent event) {
        useCase.handlePriceChange(event);
    }
}
