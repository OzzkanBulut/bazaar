package com.ozkan.bazaar.service.impl;


import com.ozkan.bazaar.config.RabbitMQConfig;
import com.ozkan.bazaar.domain.OrderStatusMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.ORDER_STATUS_QUEUE)
    public void handleOrderStatusUpdate(OrderStatusMessage message) {
        // Send order status update to customer via WebSocket
        String destination = "/topic/customer/" + message.getCustomerId();
        messagingTemplate.convertAndSend(destination, message);
    }
}
