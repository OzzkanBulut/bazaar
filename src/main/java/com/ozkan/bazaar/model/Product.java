package com.ozkan.bazaar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")  // PostgreSQL'de TEXT uzunluk sınırı olmadan kullanılabilir

    private String description;

    private int mrpPrice;

    private int sellingPrice;

    private int discountPercentage;

    private int quantity;

    private String color;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    private int numRatings;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    private LocalDateTime createdAt;

    private String sizes;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Product(String title, String description, int mrpPrice, int sellingPrice, int discountPercentage, int quantity, String color, List<String> images, int numRatings, Category category, LocalDateTime createdAt, String sizes, List<Review> reviews, Seller seller) {
    }
}
