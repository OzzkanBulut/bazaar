package com.ozkan.bazaar.model;

import com.ozkan.bazaar.domain.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDetails {

    private String paymentId;

    private String razorPaymentLinkId;

    private String razorPaymentLinkReferenceId;

    private String razorPaymentLinkStatus;

    private String razorPaymentIdZWSP;

    private PaymentStatus paymentStatus;
}
