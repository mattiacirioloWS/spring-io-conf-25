package net.springio.conference.order.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderDto(UUID id, UUID attendeeId, String status, List<OrderItemDto> items, BigDecimal total) {
}
