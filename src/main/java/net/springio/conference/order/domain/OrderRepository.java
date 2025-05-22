package net.springio.conference.order.domain;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository {
    Optional<Order> findById(OrderId orderId);

    Optional<Order> findPendingByAttendee(AttendeeId attendeeId);

    Boolean hasAttendeeOrderedItem(AttendeeId attendeeId, OrderItemId itemId);

    Order save(Order order);

    void saveAll(List<Order> orders);

    List<Order> findPendingOrdersHavingItem(UUID itemId);

    Boolean existsInCompletedOrders(ItemId itemId);
}
