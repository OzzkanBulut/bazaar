package com.ozkan.bazaar.event;

import java.io.Serializable;

public record OrderPlacedEvent(Long orderId, Long sellerId, String productName) implements Serializable {
    private static final long serialVersionUID = 1L;
}