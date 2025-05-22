package net.springio.conference.order.application;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(UUID id, String name, BigDecimal price, UUID orderId) {
}
