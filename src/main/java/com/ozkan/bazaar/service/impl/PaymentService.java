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


    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {

            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            return true;
        }
        paymentOrder.setStatus(PaymentOrderStatus.FAILED);
        paymentOrderRepository.save(paymentOrder);
        return false;
    }

}
