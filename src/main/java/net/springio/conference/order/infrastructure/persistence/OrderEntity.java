package net.springio.conference.order.infrastructure.persistence;

import jakarta.persistence.*;
import net.springio.conference.order.domain.AttendeeId;
import net.springio.conference.order.domain.OrderId;
import net.springio.conference.order.domain.OrderStatus;

import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    private OrderId id;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items;

    @Embedded
    @AttributeOverride(name = "uuid", column = @Column(name = "attendee_id"))
    private AttendeeId attendeeId;

    public OrderId getId() {
        return id;
    }

    public void setId(OrderId id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus state) {
        this.status = state;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
    }

    public AttendeeId getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(AttendeeId attendeeId) {
        this.attendeeId = attendeeId;
    }

    static class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

        @Override
        public String convertToDatabaseColumn(OrderStatus attribute) {
            return (attribute == null ? null : attribute.getName());
        }

        @Override
        public OrderStatus convertToEntityAttribute(String dbData) {
            return (dbData == null ? null : OrderStatus.from(dbData));
        }
    }
}
