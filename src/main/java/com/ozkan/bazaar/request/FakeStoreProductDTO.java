package com.ozkan.bazaar.request;

import lombok.Data;

@Data
public class FakeStoreProductDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String category;
    private String image;
    private Rating rating;

    @Data
    public static class Rating {
        private double rate;
        private int count;
    }
}