package net.springio.conference.order.domain;

import net.springio.conference.shared.domain.Price;
import org.jmolecules.ddd.annotation.Factory;

import java.util.List;

@Factory
public final class OrderFactory {

    public static Order reconstituteOrder(OrderId id, AttendeeId attendeeId, OrderStatus status, List<OrderItem> items) {
        return new Order(id, attendeeId, status, items);
    }

    public static Order createOrder(AttendeeId attendeeId) {
        return new Order(attendeeId);
    }

    public static OrderItem reconstituteOrderItem(OrderItemId id, ItemId itemId, ItemName name, Price price) {
        return new OrderItem(id, itemId, name, price);
    }

}
