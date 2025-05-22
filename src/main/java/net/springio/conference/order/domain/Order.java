package net.springio.conference.order.domain;

import net.springio.conference.order.domain.event.OrderCompleted;
import net.springio.conference.order.domain.event.OrderItemAddedEvent;
import net.springio.conference.order.domain.event.OrderItemPriceUpdatedEvent;
import net.springio.conference.order.domain.event.OrderItemRemovedEvent;
import net.springio.conference.shared.domain.AggregateRootWithDomainEvents;
import net.springio.conference.shared.domain.Price;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public final class Order extends AggregateRootWithDomainEvents {
    @Identity
    private final OrderId id;
    private final AttendeeId attendeeId;
    private final List<OrderItem> items;
    private OrderStatus status;

    Order(OrderId id, AttendeeId attendeeId, OrderStatus status, List<OrderItem> items) {
        if (id == null) {
            throw new IllegalArgumentException("OrderId cannot be null");
        }
        if (attendeeId == null) {
            throw new IllegalArgumentException("AttendeeId cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("OrderStatus cannot be null");
        }
        this.id = id;
        this.attendeeId = attendeeId;
        this.status = status;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    Order(AttendeeId attendeeId) {
        this(OrderId.create(), attendeeId, OrderStatus.pending(), new ArrayList<>());
    }

    public OrderId getId() {
        return id;
    }

    public AttendeeId getAttendeeId() {
        return attendeeId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void complete() {
        checkModifiableOrThrow();
        this.status = OrderStatus.completed();
        registerEvent(new OrderCompleted(id.uuid()));
    }

    public void cancel() {
        checkModifiableOrThrow();
        this.status = OrderStatus.cancelled();
        registerEvent(new OrderCompleted(id.uuid()));
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public OrderItem addItem(ItemId itemId, ItemName name, Price price) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        checkModifiableOrThrow();

        if (items.stream().anyMatch(item -> item.getItemId().equals(itemId))) {
            throw new IllegalStateException("Item already exists in the order");
        }
        OrderItem addedItem = new OrderItem(itemId, name, price);
        items.add(addedItem);
        registerEvent(new OrderItemAddedEvent(id.uuid(), addedItem.getId().uuid()));
        return addedItem;
    }

    public void updateItemPrice(ItemId itemId, Price price) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (isModifiable()) {
            items.replaceAll(item -> {
                if (item.getItemId().equals(itemId)) {
                    var newItem = new OrderItem(item.getId(), item.getItemId(), item.getName(), price);
                    registerEvent(new OrderItemPriceUpdatedEvent(id.uuid(), newItem.getId().uuid(), newItem.getPrice()));
                    return newItem;
                }
                return item;
            });
        }

    }

    public void removeItem(OrderItemId itemId) {
        checkModifiableOrThrow();
        if (items.removeIf(item -> item.getId().equals(itemId))) {
            registerEvent(new OrderItemRemovedEvent(id.uuid(), itemId.uuid()));
        }
    }

    private void checkModifiableOrThrow() {
        if (!isModifiable()) {
            throw new IllegalStateException("Order cannot be modified");
        }
    }

    private boolean isModifiable() {
        return status.isPending();
    }
}
