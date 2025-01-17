package com.ozkan.bazaar.service;

import com.ozkan.bazaar.exceptions.ProductException;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.request.CreateProductRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface IProductService {

    public Product createProduct(CreateProductRequest createProductRequest, Seller seller);

    public void deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product product) throws ProductException;

    public Product findProductById(Long productId) throws ProductException;

    List<Product> searchProducts(String query);

    public Page<Product> getAllProducts(
            String category,
            String brand,
            String color,
            String size,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );

    // This is for seller dashboard, seller can get his products
    List<Product> getProductBySellerId(Long sellerId);






}
