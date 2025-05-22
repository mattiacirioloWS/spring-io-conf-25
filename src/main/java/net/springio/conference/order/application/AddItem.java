package net.springio.conference.order.application;

import net.springio.conference.attendee.FindAttendees;
import net.springio.conference.order.domain.*;
import net.springio.conference.session.FindSessions;
import net.springio.conference.session.SessionDto;
import net.springio.conference.shared.application.UseCase;
import net.springio.conference.shared.domain.Price;

import java.util.UUID;

@UseCase
public class AddItem {
    private final FindAttendees findAttendees;
    private final FindSessions findSessions;
    private final OrderRepository orderRepository;
    private final OrderItemDtoMapper orderItemDtoMapper;
    private final SinglePurchasePerItemPolicy singlePurchasePerItemPolicy;

    public AddItem(FindAttendees findAttendees, FindSessions findSessions, OrderRepository orderRepository,
                   OrderItemDtoMapper orderItemDtoMapper, SinglePurchasePerItemPolicy singlePurchasePerItemPolicy) {
        this.findAttendees = findAttendees;
        this.findSessions = findSessions;
        this.orderRepository = orderRepository;
        this.orderItemDtoMapper = orderItemDtoMapper;
        this.singlePurchasePerItemPolicy = singlePurchasePerItemPolicy;
    }

    public OrderItemDto execute(UUID attendeeId, UUID itemId) {
        AttendeeId attendee = new AttendeeId(attendeeId);
        ItemId item = new ItemId(itemId);

        findAttendees.byId(attendeeId).orElseThrow(() ->
                new IllegalArgumentException("Attendee not found"));

        SessionDto session = findSessions.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        singlePurchasePerItemPolicy.check(item);

        Order order = orderRepository.findPendingByAttendee(attendee)
                .orElse(OrderFactory.createOrder(attendee));

        OrderItem orderItem = order.addItem(item, new ItemName(session.getTitle()), new Price(session.getPrice()));
        orderRepository.save(order);
        return orderItemDtoMapper.toDto(orderItem, order.getId());
    }

}
