package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.domain.PaymentMethod;
import com.ozkan.bazaar.model.*;
import com.ozkan.bazaar.repository.IPaymentOrderRepository;
import com.ozkan.bazaar.response.PaymentLinkResponse;
import com.ozkan.bazaar.service.*;
import com.razorpay.PaymentLink;
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

    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse res = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(),
                    paymentOrder.getId());
            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            res.setPayment_link_url(paymentUrl);

            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        } else {
            String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(),
                    paymentOrder.getId());

            res.setPayment_link_url(paymentUrl);
        }
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


    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

}
