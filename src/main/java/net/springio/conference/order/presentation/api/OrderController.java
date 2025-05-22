package net.springio.conference.order.presentation.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.springio.conference.order.application.AddItem;
import net.springio.conference.order.application.FindOrders;
import net.springio.conference.order.application.OrderDto;
import net.springio.conference.order.application.OrderItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
class OrderController {
    private final AddItem addItem;
    private final FindOrders findOrders;

    OrderController(AddItem addItem, FindOrders findOrders) {
        this.addItem = addItem;
        this.findOrders = findOrders;
    }

    @PutMapping
    public OrderItemDto addItem(@RequestBody @Valid AddItemCommand command) {
        return addItem.execute(command.attendeeId, command.itemId);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable UUID orderId) {
        return findOrders.findById(orderId);
    }

    @GetMapping("/byAttendee/{attendeeId}")
    public ResponseEntity<OrderDto> getOrderByAttendee(@PathVariable UUID attendeeId) {
        return findOrders.findPendingByAttendee(attendeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    record AddItemCommand(@NotNull UUID attendeeId, @NotNull UUID itemId) {
    }

}
