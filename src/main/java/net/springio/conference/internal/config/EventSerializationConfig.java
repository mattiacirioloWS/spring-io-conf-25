package net.springio.conference.internal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.modulith.events.core.EventSerializer;

@Configuration
public class EventSerializationConfig {

    @Bean
    public EventSerializer eventSerializer(ObjectMapper objectMapper) {
        return new JacksonEventSerializer(objectMapper);
    }

    private static class JacksonEventSerializer implements EventSerializer {

        private final ObjectMapper objectMapper;

        public JacksonEventSerializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        @NonNull
        public Object serialize(@NonNull Object event) {
            try {
                return objectMapper.writeValueAsString(event);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize event", e);
            }
        }

        @Override
        @NonNull
        public <T> T deserialize(@NonNull Object event, @NonNull Class<T> type) {
            try {
                return objectMapper.readValue(event.toString(), type);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize event", e);
            }
        }
    }
}
