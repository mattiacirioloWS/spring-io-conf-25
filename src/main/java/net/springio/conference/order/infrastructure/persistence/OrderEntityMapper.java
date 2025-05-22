package net.springio.conference.order.infrastructure.persistence;

import net.springio.conference.order.domain.Order;
import net.springio.conference.shared.infrastructure.EventsMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemEntityMapper.class, EventsMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface OrderEntityMapper {
    OrderEntity fromAggregate(Order orderEntity);
}
