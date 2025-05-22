package net.springio.conference.order.application;

import net.springio.conference.order.domain.Order;
import net.springio.conference.order.domain.OrderId;
import net.springio.conference.order.domain.OrderRepository;
import net.springio.conference.shared.application.UseCase;

import java.util.UUID;

@UseCase
public class CancelOrder {
    private final OrderRepository orderRepository;

    public CancelOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void execute(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId)).orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.cancel();
        orderRepository.save(order);
    }

}
