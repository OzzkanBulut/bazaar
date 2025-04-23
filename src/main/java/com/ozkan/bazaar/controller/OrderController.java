package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.domain.PaymentMethod;
import com.ozkan.bazaar.model.*;
import com.ozkan.bazaar.repository.IPaymentOrderRepository;
import com.ozkan.bazaar.request.AddressRequest;
import com.ozkan.bazaar.response.PaymentLinkResponse;
import com.ozkan.bazaar.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final ICartService cartService;
    private final ISellerService sellerService;
    private final ISellerReportService sellerReportService;
    private final IPaymentService paymentService;
    private final IPaymentOrderRepository paymentOrderRepository;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody AddressRequest addressRequest,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        Address shippingAddress = userService.findAddressById(addressRequest.getAddressId());
        if (shippingAddress == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PaymentLinkResponse("Address not found"));
        }

        // 1. Create Order objects (but do not persist yet)
        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        // 2. Create PaymentOrder and set its details
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount((long) cart.getTotalMrpPrice()); // Or use proper calculation
        paymentOrder.setPaymentMethod(PaymentMethod.STRIPE); // Or dynamic from frontend
        paymentOrder.setOrders(orders); // Assign orders to PaymentOrder

        // 3. Set the paymentOrder in each Order
        for (Order order : orders) {
            order.setPaymentOrder(paymentOrder);
        }

        // 4. Save PaymentOrder (which cascades to Orders)
        String paymentUrl = "payment_url";
        String paymentUrlId = "payment_url_id";
        paymentOrder.setPaymentLinkId(paymentUrlId);
        paymentOrderRepository.save(paymentOrder);

        // 5. Prepare response
        PaymentLinkResponse res = new PaymentLinkResponse();
        res.setPayment_link_url(paymentUrl);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        OrderItem item = orderService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
