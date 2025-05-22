package net.springio.conference.order.config;

import net.springio.conference.attendee.FindAttendees;
import net.springio.conference.order.application.AddItem;
import net.springio.conference.order.application.FindOrders;
import net.springio.conference.order.application.OrderDtoMapper;
import net.springio.conference.order.application.OrderItemDtoMapper;
import net.springio.conference.order.domain.OrderRepository;
import net.springio.conference.session.FindSessions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {
    @Bean
    FindOrders findOrders(OrderRepository orderRepository, OrderDtoMapper orderDtoMapper) {
        return new FindOrders(orderRepository, orderDtoMapper);
    }

    @Bean
    AddItem addItem(FindAttendees findAttendees, FindSessions findSessions, OrderRepository orderRepository, OrderItemDtoMapper orderItemDtoMapper) {
        return new AddItem(findAttendees, findSessions, orderRepository, orderItemDtoMapper);
    }
}
