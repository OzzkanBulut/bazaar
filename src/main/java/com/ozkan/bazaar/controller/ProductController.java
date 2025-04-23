package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.exceptions.ProductException;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {

        Product product = productService.findProductById(productId);

        return new ResponseEntity<>(product, HttpStatus.OK);

    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query){

        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getALlProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue ="0") Integer pageNumber
    ){
        return new ResponseEntity<>(
                productService.getAllProducts(category, brand, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber),
                HttpStatus.OK
        );
    }


}
