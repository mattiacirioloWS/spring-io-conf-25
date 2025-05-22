package net.springio.conference.order.presentation;

import net.springio.conference.order.domain.event.OrderItemPriceUpdatedEvent;
import net.springio.conference.order.domain.event.OrderTotalChangedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Component
public class PriceChangesNotifier implements ApplicationListener<ContextClosedEvent> {
    private final Map<UUID, SseEmitter> orderEmitters = new ConcurrentHashMap<>();

    public SseEmitter streamItemPriceChanges(UUID orderId) {
        SseEmitter emitter = new SseEmitter(0L);
        orderEmitters.put(orderId, emitter);

        emitter.onCompletion(() -> orderEmitters.remove(orderId));
        emitter.onTimeout(() -> orderEmitters.remove(orderId));
        emitter.onError(e -> orderEmitters.remove(orderId));

        return emitter;
    }

    @ApplicationModuleListener
    void onItemPriceChanged(OrderItemPriceUpdatedEvent event) {
        orderEmitters.computeIfPresent(event.orderId(), sendEvent(event));
    }

    private BiFunction<UUID, SseEmitter, SseEmitter> sendEvent(OrderItemPriceUpdatedEvent event) {
        return (id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("orderItemPriceChanged").data(event));
            } catch (IOException e) {
                orderEmitters.remove(id);
            }
            return null;
        };
    }

    @ApplicationModuleListener
    void onOrderTotalChanged(OrderTotalChangedEvent event) {
        try {
            orderEmitters.get(event.orderId()).send(SseEmitter.event().name("orderTotalChanged").data(event));
        } catch (IOException e) {
            orderEmitters.remove(event.orderId());
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("Closing up all SSE emitters");
        orderEmitters.values().forEach(SseEmitter::complete);
        orderEmitters.clear();
    }
}
