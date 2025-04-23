package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.exceptions.ProductException;
import com.ozkan.bazaar.model.Category;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.repository.ICategoryRepository;
import com.ozkan.bazaar.repository.IProductRepository;
import com.ozkan.bazaar.repository.ISellerRepository;
import com.ozkan.bazaar.request.CreateProductRequest;
import com.ozkan.bazaar.service.IProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final ISellerRepository sellerRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {

        // Find the parent category first
        Category parentCategory = null;

        if (req.getCategories() !=null && !req.getCategories().isEmpty()) {
            parentCategory = categoryRepository.findByCategoryId(req.getCategories().get(0));

            if (parentCategory == null) {
                parentCategory = new Category();
                parentCategory.setCategoryId(req.getCategories().get(0));
                parentCategory.setLevel(1);
                categoryRepository.save(parentCategory);
            }

            Category currentCategory = parentCategory;

            for (int i = 1; i < req.getCategories().size(); i++) {
                String categoryId = req.getCategories().get(i);

                Category childCategory = categoryRepository.findByCategoryId(categoryId);

                if (childCategory == null) {
                    childCategory = new Category();
                    childCategory.setCategoryId(categoryId);
                    childCategory.setLevel(i + 1);
                    childCategory.setParentCategory(currentCategory);
                    currentCategory = categoryRepository.save(childCategory);
                } else {
                    currentCategory = childCategory;
                }
            }
        }

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(parentCategory);  // Set the top-level category
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercentage(discountPercentage);

        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0){
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }

        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {

        Product product = findProductById(productId);
        productRepository.delete(product);
    }


    @Override
    public Product updateProduct(Long productId, Long sellerId, Product updatedProduct) {
        // Find the existing product
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Ensure the seller owns the product
        if (!existingProduct.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You can only update your own products");
        }

        // Update only the fields that are provided (not null)
        if (updatedProduct.getTitle() != null) {
            existingProduct.setTitle(updatedProduct.getTitle());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getMrpPrice() != 0) {
            existingProduct.setMrpPrice(updatedProduct.getMrpPrice());
        }
        if (updatedProduct.getSellingPrice() != 0) {
            existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
        }
        if (updatedProduct.getDiscountPercentage() != 0) {
            existingProduct.setDiscountPercentage(updatedProduct.getDiscountPercentage());
        }
        if (updatedProduct.getQuantity() != 0) {
            existingProduct.setQuantity(updatedProduct.getQuantity());
        }
        if (updatedProduct.getColor() != null) {
            existingProduct.setColor(updatedProduct.getColor());
        }
        if (updatedProduct.getImages() != null && !updatedProduct.getImages().isEmpty()) {
            existingProduct.setImages(updatedProduct.getImages());
        }
        if (updatedProduct.getNumRatings() != 0) {
            existingProduct.setNumRatings(updatedProduct.getNumRatings());
        }
        if (updatedProduct.getCategory() != null) {
            existingProduct.setCategory(updatedProduct.getCategory());
        }
        if (updatedProduct.getSizes() != null) {
            existingProduct.setSizes(updatedProduct.getSizes());
        }

        // Save the updated product
        return productRepository.save(existingProduct);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductException("Product not found with id:"+productId)
        );
    }

    @Override
    public List<Product> searchProducts(String query) {

        return productRepository.searchProduct(query);

    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String color, String size, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {

        // FILTERING FEATURE
        Specification<Product> spec = (root,query,criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();

            if(category!=null){
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"),category));
            }

            if(color !=null && !color.isEmpty()){
                System.out.println("color:"+color);
                predicates.add(criteriaBuilder.equal(root.get("color"),color));

            }

            // Filter by size
            if(size !=null && !size.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("size"),size));
            }

            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),minPrice));
            }

            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),maxPrice));
            }

            if(minDiscount !=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"),minDiscount));
            }

            if(stock != null){
                predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

        // SORTING FEATURE
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {

            pageable = switch (sort) {
                case "price_low" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
            };
        }else{
            pageable = PageRequest.of(pageNumber!= null ? pageNumber:0,10,Sort.unsorted());
        }

        return productRepository.findAll(spec,pageable);

    }

    @Override
    public Page<Product> getProductsBySellerId(Long sellerId, Pageable pageable) {
        return productRepository.findBySellerId(sellerId,pageable);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));
    }


}
