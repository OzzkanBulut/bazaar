package com.ozkan.bazaar.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusMessage {

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("customerId")
    private String customerId;
}