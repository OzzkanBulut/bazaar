package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Category;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.repository.ICategoryRepository;
import com.ozkan.bazaar.repository.IProductRepository;
import com.ozkan.bazaar.repository.ISellerRepository;
import com.ozkan.bazaar.request.FakeStoreProductDTO;
import com.ozkan.bazaar.service.ISellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FakeStoreService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ISellerService sellerService;

    private static final String FAKE_STORE_URL = "https://fakestoreapi.com/products";

    public List<FakeStoreProductDTO> fetchProducts() {
        FakeStoreProductDTO[] products = restTemplate.getForObject(FAKE_STORE_URL, FakeStoreProductDTO[].class);
        return products != null ? List.of(products) : Collections.emptyList();
    }

    public void importProducts(String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);

        List<FakeStoreProductDTO> fakeProducts = fetchProducts();

        for (FakeStoreProductDTO fakeProduct : fakeProducts) {
            String mappedCategoryName = CategoryMapper.mapCategory(fakeProduct.getCategory(), fakeProduct.getTitle());
            Category category = null;
            if(categoryRepository.findByCategoryId(mappedCategoryName)!=null){
                category = categoryRepository.findByCategoryId(mappedCategoryName);
            }else{
                category = new Category();
                category.setName(mappedCategoryName);
                category.setCategoryId(mappedCategoryName);
                category.setLevel(1);
                categoryRepository.save(category);
            }


            Product product = new Product();
            product.setTitle(fakeProduct.getTitle());
            product.setDescription(fakeProduct.getDescription().substring(0, Math.min(fakeProduct.getDescription().length(), 255)));
            product.setMrpPrice((int) fakeProduct.getPrice() + 50);
            product.setSellingPrice((int) fakeProduct.getPrice());
            product.setDiscountPercentage(10);
            product.setQuantity(100);
            product.setColor("Black");
            product.setImages(Collections.singletonList(fakeProduct.getImage()));
            product.setNumRatings(fakeProduct.getRating().getCount());
            product.setCategory(category);
            product.setSeller(seller);
            product.setCreatedAt(LocalDateTime.now());
            product.setSizes("M,L,XL");

            productRepository.save(product);
        }
    }
}
