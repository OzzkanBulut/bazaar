package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.domain.PaymentOrderStatus;
import com.ozkan.bazaar.model.*;
import com.ozkan.bazaar.repository.IOrderRepository;
import com.ozkan.bazaar.repository.IPaymentOrderRepository;
import com.ozkan.bazaar.service.IPaymentService;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;


import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final IPaymentOrderRepository paymentOrderRepository;
    private final IOrderRepository orderRepository;
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;


    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId).orElseThrow(
                () -> new Exception("Payment order not found ")
        );
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(orderId);
        if (paymentOrder == null) {
            throw new Exception("Payment order not found with provided payment link id:" + orderId);
        }
        return paymentOrder;
    }





    public String createStripeSession(PaymentOrder paymentOrder,long totalAmountInCents) throws Exception {
        // Stripe line item'ları için bir liste oluştur
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
         // Toplam tutarı hesaplamak için bir değişken
        int i = 0;

        // PaymentOrder'daki her Order'ı işle
        for (Order order : paymentOrder.getOrders()) {
            for (OrderItem orderItem : order.getOrderItems()) {
                // Her OrderItem'dan gelen ürün bilgilerini al
                long itemAmountInCents = orderItem.getSellingPrice() * 100;  // Ürünün fiyatını cent cinsine çevir
                totalAmountInCents += (itemAmountInCents * orderItem.getQuantity())+5000;  // Toplam ödeme tutarını güncelle

                // Her bir ürün için bir LineItem oluştur
                lineItems.add(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(itemAmountInCents)  // Ürünün birim fiyatı
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(orderItem.getProduct().getDescription())  // Ürünün adı
                                                                .setDescription(orderItem.getProduct().getDescription())  // Ürün açıklaması
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity((long) orderItem.getQuantity())  // Ürünün sipariş edilen miktarı
                                .build()
                );
            }
        }


        // Stripe session'ını oluştur
        Session session = Session.create(
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .addAllLineItem(lineItems)  // Dinamik olarak oluşturduğumuz line item'ları ekle
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://bazaar-front.vercel.app/order-success")
                        .setCancelUrl("https://bazaar-front.vercel.app/cart")
                        .build()
        );

        // Toplam tutarı doğrulamak için loglama yapılabilir (isteğe bağlı)
        System.out.println("Total amount (in cents): " + totalAmountInCents);


        return session.getUrl();  // Stripe Checkout URL’sini döndür
    }




}
