package net.springio.conference.order.domain;

import net.springio.conference.shared.domain.Price;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public final class OrderItem {
    @Identity
    private final OrderItemId id;
    private final ItemId itemId;
    private final ItemName name;
    private final Price price;

    OrderItem(OrderItemId id, ItemId itemId, ItemName name, Price price) {
        if (id == null) {
            throw new IllegalArgumentException("OrderItemId cannot be null");
        }
        if (itemId == null) {
            throw new IllegalArgumentException("SessionId cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("ItemName cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        this.id = id;
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    OrderItem(ItemId itemId, ItemName name, Price price) {
        this(OrderItemId.create(), itemId, name, price);
    }

    public OrderItemId getId() {
        return id;
    }

    public ItemId getItemId() {
        return itemId;
    }

    public ItemName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
