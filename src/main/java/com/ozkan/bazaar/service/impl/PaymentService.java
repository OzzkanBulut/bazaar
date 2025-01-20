package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.domain.PaymentOrderStatus;
import com.ozkan.bazaar.domain.PaymentStatus;
import com.ozkan.bazaar.model.Order;
import com.ozkan.bazaar.model.PaymentOrder;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.IOrderRepository;
import com.ozkan.bazaar.repository.IPaymentOrderRepository;
import com.ozkan.bazaar.service.IPaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final IPaymentOrderRepository paymentOrderRepository;
    private final IOrderRepository orderRepository;
    private String apiKey="apikey";
    private String apiSecret="apisecret";
    private String stripeSecretKey = "stripesecretkey";


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
                ()-> new Exception("Payment order not found ")
        );
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder paymentOrder =  paymentOrderRepository.findByPaymentLinkId(orderId);
        if(paymentOrder == null){
            throw new Exception("Payment order not found with provided payment link id:"+orderId);
        }
        return paymentOrder;
    }


    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);

            Payment payment = razorpay.payments.fetch(paymentId);

            String status = payment.get("status");
            if(status.equals("captured")){
                Set<Order> orders = paymentOrder.getOrders();
                for(Order order:orders){
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100;
        try{
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","USD");

            JSONObject customer = new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());

            paymentLinkRequest.put("customer",customer);

            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success/"+orderId);
            paymentLinkRequest.put("callback_method","get");

            com.razorpay.PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");

            return paymentLink;
        }catch (Exception e){
            throw new RazorpayException(e.getMessage());
        }


    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel/")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(
                                        SessionCreateParams
                                                .LineItem.PriceData.ProductData.builder()
                                                .setName("The bazaar payment")
                                                .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
