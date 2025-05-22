package net.springio.conference.order.infrastructure.persistence;

import jakarta.persistence.*;
import net.springio.conference.order.domain.ItemId;
import net.springio.conference.order.domain.ItemName;
import net.springio.conference.order.domain.OrderItemId;
import net.springio.conference.shared.domain.Price;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    private OrderItemId id;

    @Embedded
    @AttributeOverride(name = "uuid", column = @Column(name = "item_id"))
    private ItemId itemId;

    @Embedded
    private ItemName name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Price price;

    public OrderItemId getId() {
        return id;
    }

    public void setId(OrderItemId id) {
        this.id = id;
    }

    public ItemId getItemId() {
        return itemId;
    }

    public void setItemId(ItemId itemId) {
        this.itemId = itemId;
    }

    public ItemName getName() {
        return name;
    }

    public void setName(ItemName name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
