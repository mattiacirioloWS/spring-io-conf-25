package net.springio.conference.order.application;

import net.springio.conference.order.domain.ItemId;
import net.springio.conference.order.domain.Order;
import net.springio.conference.order.domain.OrderRepository;
import net.springio.conference.order.domain.port.PriceChangePolicyPort;
import net.springio.conference.session.domain.event.PriceChangedEvent;
import net.springio.conference.shared.application.UseCase;

import java.util.List;

@UseCase
public class PriceChangesHandler implements PriceChangePolicyPort {
    private final OrderRepository orderRepository;

    public PriceChangesHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void handlePriceChange(PriceChangedEvent event) {
        List<Order> pendingOrdersHavingItem = orderRepository.findPendingOrdersHavingItem(event.session());
        pendingOrdersHavingItem
                .forEach(order -> order.updateItemPrice(new ItemId(event.session()), event.newPrice()));
        orderRepository.saveAll(pendingOrdersHavingItem);
    }
}
