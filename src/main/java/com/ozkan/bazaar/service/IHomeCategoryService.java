package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.HomeCategory;

import java.util.List;

public interface IHomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception;
    List<HomeCategory> getAllHomeCategories();
    HomeCategory getHomeCategoryById(Long id) throws Exception;
}
