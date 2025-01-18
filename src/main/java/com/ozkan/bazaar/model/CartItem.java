package com.ozkan.bazaar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"cart"}) // Exclude circular reference to Cart
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Cart cart;


    @ManyToOne
    private Product product;

    private String size;

    private int quantity = 1;

    private Integer mrpPrice;

    private Integer sellingPrice;

    private Long userId;



}
