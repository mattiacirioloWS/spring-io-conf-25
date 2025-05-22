package net.springio.conference.order.domain;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository {
    Optional<Order> findById(OrderId orderId);

    Optional<Order> findPendingByAttendee(AttendeeId attendeeId);

    Boolean hasAttendeeOrderedItem(AttendeeId attendeeId, OrderItemId itemId);

    Order save(Order order);
}
