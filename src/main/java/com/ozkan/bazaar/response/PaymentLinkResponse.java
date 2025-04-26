package com.ozkan.bazaar.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkResponse {

    private String payment_link_url;
    private String payment_link_id;
    private Long payment_order_id;

    public PaymentLinkResponse(String addressNotFound) {

    }
}
