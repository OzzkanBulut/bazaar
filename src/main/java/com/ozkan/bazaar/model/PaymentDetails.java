package com.ozkan.bazaar.model;

import com.ozkan.bazaar.domain.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDetails {

    private String paymentId;

    private PaymentStatus paymentStatus;
}
