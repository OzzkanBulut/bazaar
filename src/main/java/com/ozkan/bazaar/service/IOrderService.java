package com.ozkan.bazaar.service;

import com.ozkan.bazaar.domain.OrderStatus;
import com.ozkan.bazaar.model.*;

import java.util.List;
import java.util.Set;

public interface IOrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long id) throws Exception;
    List<Order> userOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);

    Order cancelOrder(Long orderId, User user) throws Exception;
    OrderItem getOrderItemById(Long id) throws Exception;


}
