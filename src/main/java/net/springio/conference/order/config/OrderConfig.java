package net.springio.conference.order.config;

import net.springio.conference.attendee.FindAttendees;
import net.springio.conference.order.application.*;
import net.springio.conference.order.domain.OrderRepository;
import net.springio.conference.order.domain.port.PriceChangePolicyPort;
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
    AddItem addItem(FindAttendees findAttendees, FindSessions findSessions, OrderRepository orderRepository,
                    OrderItemDtoMapper orderItemDtoMapper, SinglePurchasePerItemPolicy singlePurchasePerItemPolicy) {
        return new AddItem(findAttendees, findSessions, orderRepository, orderItemDtoMapper, singlePurchasePerItemPolicy);
    }

    @Bean
    PriceChangePolicyPort priceChangesHandler(OrderRepository orderRepository) {
        return new PriceChangesHandler(orderRepository);
    }

    @Bean
    SubmitOrder submitOrder(OrderRepository orderRepository) {
        return new SubmitOrder(orderRepository);
    }

    @Bean
    CancelOrder cancelOrder(OrderRepository orderRepository) {
        return new CancelOrder(orderRepository);
    }

    @Bean
    SinglePurchasePerItemPolicy singlePurchasePerItemPolicy(OrderRepository orderRepository) {
        return new SinglePurchasePerItemPolicy(orderRepository);
    }
}
