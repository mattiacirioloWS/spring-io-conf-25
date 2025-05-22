package net.springio.conference.order.application;

import net.springio.conference.order.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {OrderItemDtoMapper.class})
public interface OrderDtoMapper {
    @Mapping(target = "id", source = "id.uuid")
    @Mapping(target = "attendeeId", source = "attendeeId.uuid")
    @Mapping(target = "status", source = "status.name")
    OrderDto toDto(Order order);
}
