package net.springio.conference.order.presentation.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.springio.conference.order.application.*;
import net.springio.conference.order.presentation.PriceChangesNotifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
class OrderController {
    private final AddItem addItem;
    private final FindOrders findOrders;
    private final SubmitOrder submitOrder;
    private final CancelOrder cancelOrder;
    private final PriceChangesNotifier itemPriceChangesNotifier;

    OrderController(AddItem addItem, FindOrders findOrders, SubmitOrder submitOrder, CancelOrder cancelOrder, PriceChangesNotifier itemPriceChangesNotifier) {
        this.addItem = addItem;
        this.findOrders = findOrders;
        this.submitOrder = submitOrder;
        this.cancelOrder = cancelOrder;
        this.itemPriceChangesNotifier = itemPriceChangesNotifier;
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

    @GetMapping(value = "/{orderId}/changes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOrderTotal(@PathVariable UUID orderId) {
        return itemPriceChangesNotifier.streamItemPriceChanges(orderId);
    }

    @PutMapping("/{orderId}/submit")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void submitOrder(@PathVariable UUID orderId) {
        submitOrder.execute(orderId);
    }

    @DeleteMapping("/{orderId}/cancel")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable UUID orderId) {
        cancelOrder.execute(orderId);
    }

    record AddItemCommand(@NotNull UUID attendeeId, @NotNull UUID itemId) {
    }
}
