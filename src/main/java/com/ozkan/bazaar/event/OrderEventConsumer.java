package com.ozkan.bazaar.event;

import com.ozkan.bazaar.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;  // Inject ObjectMapper

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderPlaced(Message message) {
        try {
            // Deserialize the message body into an OrderPlacedEvent object
            OrderPlacedEvent event = objectMapper.readValue(message.getBody(), OrderPlacedEvent.class);
            System.out.println("🔔 New Order Event: " + event);

            // Send the message to the seller's topic
            messagingTemplate.convertAndSend(
                    "/topic/seller/" + event.sellerId(),
                    "New order for: " + event.productName()
            );
        } catch (Exception e) {
            e.printStackTrace();
            // Handle deserialization errors and other exceptions
        }
    }
}