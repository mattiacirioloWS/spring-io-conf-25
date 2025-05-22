package net.springio.conference.order.infrastructure.persistence;

import net.springio.conference.order.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderEntityMapper orderEntityMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId)
                .map(this::toAggregate);
    }

    @Override
    public Optional<Order> findPendingByAttendee(AttendeeId attendeeId) {
        return orderJpaRepository.findByAttendeeIdAndStatus(attendeeId, OrderStatus.pending())
                .map(this::toAggregate);
    }

    @Override
    public Boolean hasAttendeeOrderedItem(AttendeeId attendeeId, OrderItemId itemId) {
        return orderJpaRepository.existsByAttendeeIdAndItemsId(attendeeId, itemId);
    }

    @Override
    public Order save(Order order) {
        OrderEntity savedOrder = orderJpaRepository.save(orderEntityMapper.fromAggregate(order));
        return toAggregate(savedOrder);
    }

    private Order toAggregate(OrderEntity orderEntity) {
        return OrderFactory.reconstituteOrder(
                orderEntity.getId(),
                orderEntity.getAttendeeId(),
                orderEntity.getStatus(),
                toOrderItemAggregates(orderEntity.getItems())
        );
    }

    private List<OrderItem> toOrderItemAggregates(List<OrderItemEntity> orderItemEntities) {
        if (orderItemEntities == null) {
            return new ArrayList<>();
        }
        return orderItemEntities.stream()
                .map(orderItemEntity -> OrderFactory.reconstituteOrderItem(
                        orderItemEntity.getId(),
                        orderItemEntity.getItemId(),
                        orderItemEntity.getName(),
                        orderItemEntity.getPrice()
                ))
                .toList();
    }
}
