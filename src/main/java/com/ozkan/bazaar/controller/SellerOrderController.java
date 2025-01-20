package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.domain.OrderStatus;
import com.ozkan.bazaar.model.Order;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.service.IOrderService;
import com.ozkan.bazaar.service.ISellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final IOrderService orderService;
    private final ISellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellersOrder(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
    ) throws Exception {

        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }
}
