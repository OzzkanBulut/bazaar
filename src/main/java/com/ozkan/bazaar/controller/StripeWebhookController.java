//package com.ozkan.bazaar.controller;
//
//import com.ozkan.bazaar.event.OrderPlacedEvent;
//import com.ozkan.bazaar.model.Order;
//import com.ozkan.bazaar.model.OrderItem;
//import com.ozkan.bazaar.service.impl.OrderEventPublisher;
//import com.ozkan.bazaar.service.impl.OrderService;
//import com.stripe.exception.SignatureVerificationException;
//import com.stripe.model.Event;
//import com.stripe.net.Webhook;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.stripe.model.checkout.Session;
//
//
//@RestController
//@RequestMapping("/api/payment")
//public class StripeWebhookController {
//
//    @Value("${stripe.webhook.secret}")
//    private String endpointSecret;
//
//    private final OrderEventPublisher orderEventPublisher;
//    private final OrderService orderService; // Optional, if you want to fetch order info
//
//    public StripeWebhookController(OrderEventPublisher orderEventPublisher, OrderService orderService) {
//        this.orderEventPublisher = orderEventPublisher;
//        this.orderService = orderService;
//    }
//
//    @PostMapping("/webhook")
//    public ResponseEntity<String> handleStripeWebhook(
//            @RequestBody String payload,
//            @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {
//
//        Event event;
//
//        try {
//            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
//        } catch (SignatureVerificationException e) {
//            return ResponseEntity.badRequest().body("Invalid signature");
//        }
//
//        // ✅ Handle successful payment
//        if ("checkout.session.completed".equals(event.getType())) {
//            Session session = (Session) event.getDataObjectDeserializer()
//                    .getObject()
//                    .orElse(null);
//
//
//            if (session != null) {
//                String clientReferenceId = session.getClientReferenceId(); // Your order ID (you should set this when creating the session)
//
//                // 👇 Fetch the order using that ID (or session metadata if you prefer)
//                Order savedOrder = orderService.findOrderById(Long.parseLong(clientReferenceId));
//                OrderItem firstItem = savedOrder.getOrderItems().get(0);
//                Long sellerId = firstItem.getProduct().getSeller().getId();
//                String productTitle = firstItem.getProduct().getTitle();
//
//                // 🔔 Publish notification
//                orderEventPublisher.publishOrderPlacedEvent(
//                        new OrderPlacedEvent(savedOrder.getId(), sellerId, productTitle)
//                );
//
//                System.out.println("✅ Seller alerted after successful Stripe payment.");
//            }
//        }
//
//        return ResponseEntity.ok("Webhook received");
//    }
//}
//
