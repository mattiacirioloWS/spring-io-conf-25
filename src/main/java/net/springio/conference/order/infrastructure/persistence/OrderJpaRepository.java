package net.springio.conference.order.infrastructure.persistence;

import net.springio.conference.order.domain.AttendeeId;
import net.springio.conference.order.domain.OrderId;
import net.springio.conference.order.domain.OrderItemId;
import net.springio.conference.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, OrderId> {
    Optional<OrderEntity> findByAttendeeIdAndStatus(AttendeeId attendeeId, OrderStatus status);

    Boolean existsByAttendeeIdAndItemsId(AttendeeId attendeeId, OrderItemId itemId);
}
