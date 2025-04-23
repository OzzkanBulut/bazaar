package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Order;
import com.ozkan.bazaar.model.PaymentOrder;
import com.ozkan.bazaar.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface IPaymentService {

    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;




}
