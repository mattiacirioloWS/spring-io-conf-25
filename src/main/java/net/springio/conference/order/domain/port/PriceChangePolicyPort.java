package net.springio.conference.order.domain.port;

import net.springio.conference.session.domain.event.PriceChangedEvent;

public interface PriceChangePolicyPort {
    void handlePriceChange(PriceChangedEvent event);
}
