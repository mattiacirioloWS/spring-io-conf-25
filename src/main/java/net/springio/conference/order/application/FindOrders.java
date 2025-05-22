package net.springio.conference.order.application;

import net.springio.conference.order.domain.AttendeeId;
import net.springio.conference.order.domain.OrderId;
import net.springio.conference.order.domain.OrderRepository;
import net.springio.conference.shared.application.UseCase;

import java.util.Optional;
import java.util.UUID;

@UseCase
public class FindOrders {
    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;

    public FindOrders(OrderRepository orderRepository, OrderDtoMapper orderDtoMapper) {
        this.orderRepository = orderRepository;
        this.orderDtoMapper = orderDtoMapper;
    }

    public OrderDto findById(UUID id) {
        return orderRepository.findById(new OrderId(id))
                .map(orderDtoMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public Optional<OrderDto> findPendingByAttendee(UUID attendeeId) {
        return orderRepository.findPendingByAttendee(new AttendeeId(attendeeId))
                .map(orderDtoMapper::toDto);
    }

}
