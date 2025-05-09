package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.config.RabbitMQConfig;
import com.ozkan.bazaar.domain.OrderStatus;
import com.ozkan.bazaar.domain.OrderStatusMessage;
import com.ozkan.bazaar.domain.PaymentStatus;
import com.ozkan.bazaar.event.OrderPlacedEvent;
import com.ozkan.bazaar.model.*;
import com.ozkan.bazaar.repository.IAddressRepository;
import com.ozkan.bazaar.repository.IOrderItemRepository;
import com.ozkan.bazaar.repository.IOrderRepository;
import com.ozkan.bazaar.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IAddressRepository addressRepository;
    private final IOrderItemRepository orderItemRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }

        Address address = addressRepository.save(shippingAddress);

// Since this is a multi-vendor app, For every seller, there must be a new order.
// A user can create orders from different sellers, so, we need to keep sellers and the order from seller
// brand 1 --> 3 shirts
// brand 2 --> 5 pants
// brand 3 --> 1 watch
        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct()
                        .getSeller().getId()));

        Set<Order> orders = new HashSet<>();
        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();

            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setPaymentStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);

            }
            orderEventPublisher.publishOrderPlacedEvent(
                    new OrderPlacedEvent(savedOrder.getId(), sellerId, items.get(0).getProduct().getTitle())
            );
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {

        return orderRepository.findById(id).orElseThrow(
                () -> new Exception("Order not found with id:" + id)
        );
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findOrderById(orderId);
        order.setOrderStatus(orderStatus);

        Order savedOrder = orderRepository.save(order);
        OrderStatusMessage message = new OrderStatusMessage(
                savedOrder.getId(),
                orderStatus,
                savedOrder.getUser().getId().toString() // Assuming you want to alert the customer
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_STATUS_QUEUE, message);
        return savedOrder;
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = orderRepository.findOrderById(orderId);

        if (!user.getId().equals(order.getUser().getId())) {
            throw new Exception("You don't have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(
                () -> new Exception("Cannot find item by id:" + id)
        );
    }
}
