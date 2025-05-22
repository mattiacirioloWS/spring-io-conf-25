package net.springio.conference.order.application;

import net.springio.conference.order.domain.ItemName;
import net.springio.conference.order.domain.OrderId;
import net.springio.conference.order.domain.OrderItem;
import net.springio.conference.order.domain.OrderItemId;
import net.springio.conference.shared.domain.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderItemDtoMapper {
    @Mapping(target = "id", source = "orderItem.id")
    @Mapping(target = "name", source = "orderItem.name")
    @Mapping(target = "price", source = "orderItem.price")
    @Mapping(target = "orderId", source = "orderId")
    OrderItemDto toDto(OrderItem orderItem, OrderId orderId);

    default UUID map(OrderItemId id) {
        return id.uuid();
    }

    default String map(ItemName name) {
        return name.name();
    }

    default BigDecimal map(Price price) {
        return price.amount();
    }

    default UUID map(OrderId id) {
        return id.uuid();
    }
}
