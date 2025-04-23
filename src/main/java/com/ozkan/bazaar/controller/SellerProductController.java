package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.exceptions.ProductException;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.request.CreateProductRequest;
import com.ozkan.bazaar.service.IProductService;
import com.ozkan.bazaar.service.ISellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/products") 
public class SellerProductController {

    private final IProductService productService;
    private final ISellerService sellerService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProductsBySellerId(
            @RequestHeader("Authorization") String jwt,
            Pageable pageable
    ) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);

        Page<Product> products = productService.getProductsBySellerId(seller.getId(), pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest productRequest,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(productRequest, seller);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        productService.getProductById(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product,
            @RequestHeader("Authorization") String jwt) throws Exception {

        // Extract seller ID from the JWT token (implement this method)
        Seller seller = sellerService.getSellerProfile(jwt);
        Long sellerId = seller.getId();

        // Call service to update product
        Product updatedProduct = productService.updateProduct(productId, sellerId, product);

        return ResponseEntity.ok(updatedProduct);
    }



}
