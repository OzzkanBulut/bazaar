package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Home;
import com.ozkan.bazaar.model.HomeCategory;
import com.ozkan.bazaar.service.IHomeCategoryService;
import com.ozkan.bazaar.service.IHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeCategoryController {

    private final IHomeCategoryService homeCategoryService;
    private final IHomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories
    ) {

        List<HomeCategory> categories = homeCategoryService.createHomeCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);

    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategories() {
        return ResponseEntity.ok(homeCategoryService.getAllHomeCategories());
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @RequestBody HomeCategory homeCategory,
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(homeCategoryService.updateHomeCategory(homeCategory, id));
    }




}
