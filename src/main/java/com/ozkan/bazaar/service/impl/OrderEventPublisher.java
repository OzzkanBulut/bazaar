package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.config.RabbitMQConfig;
import com.ozkan.bazaar.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishOrderPlacedEvent(OrderPlacedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, event);
    }

}

