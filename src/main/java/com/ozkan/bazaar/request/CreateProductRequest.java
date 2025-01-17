package com.ozkan.bazaar.request;

import com.ozkan.bazaar.model.Category;
import com.ozkan.bazaar.model.Review;
import com.ozkan.bazaar.model.Seller;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateProductRequest {

    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private String color;
    private List<String> images;
    private List<String> categories;  // Changed from separate category fields to a list

    private String sizes;


}
