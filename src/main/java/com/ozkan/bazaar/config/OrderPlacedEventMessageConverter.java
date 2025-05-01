package com.ozkan.bazaar.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozkan.bazaar.event.OrderPlacedEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

public class OrderPlacedEventMessageConverter implements MessageConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        try {
            // Convert OrderPlacedEvent to JSON string
            if (object instanceof OrderPlacedEvent event) {
                String json = objectMapper.writeValueAsString(event);
                return MessageBuilder.withBody(json.getBytes())
                        .setContentType("application/json")
                        .build();
            } else {
                throw new MessageConversionException("Invalid object type: " + object.getClass());
            }
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert OrderPlacedEvent to Message", e);
        }
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            // Convert JSON string back to OrderPlacedEvent
            return objectMapper.readValue(message.getBody(), OrderPlacedEvent.class);
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert Message to OrderPlacedEvent", e);
        }
    }
}
