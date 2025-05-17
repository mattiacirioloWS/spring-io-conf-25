package net.springio.conference.order.infrastructure.persistence;

import net.springio.conference.order.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, OrderId> {
    Optional<OrderEntity> findByAttendeeIdAndStatus(AttendeeId attendeeId, OrderStatus status);

    Boolean existsByAttendeeIdAndItemsId(AttendeeId attendeeId, OrderItemId itemId);

    List<OrderEntity> findAllByStatusAndItemsItemId(OrderStatus status, ItemId itemId);
}
