package net.springio.conference.order.infrastructure.persistence;

import net.springio.conference.order.domain.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface OrderItemEntityMapper {
    OrderItemEntity fromAggregate(OrderItem orderItem);
}
