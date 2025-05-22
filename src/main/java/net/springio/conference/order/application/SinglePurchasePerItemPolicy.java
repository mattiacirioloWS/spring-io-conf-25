package net.springio.conference.order.application;

import net.springio.conference.order.domain.ItemId;
import net.springio.conference.order.domain.OrderRepository;
import net.springio.conference.shared.application.Policy;

@Policy
public class SinglePurchasePerItemPolicy {
    private final OrderRepository orderRepository;

    public SinglePurchasePerItemPolicy(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void check(ItemId itemId) {
        if (orderRepository.existsInCompletedOrders(itemId)) {
            throw new IllegalStateException("Item was already ordered");
        }
    }
}
