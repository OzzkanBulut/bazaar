package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.HomeCategory;
import com.ozkan.bazaar.repository.IHomeCategoryRepository;
import com.ozkan.bazaar.service.IHomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryService implements IHomeCategoryService {

    private final IHomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories) {

        if (homeCategoryRepository.findAll().isEmpty()) {
            return homeCategoryRepository.saveAll(homeCategories);
        }

        return homeCategoryRepository.findAll();

    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory toUpdate = getHomeCategoryById(id);

        if (homeCategory.getImage() != null) {
            toUpdate.setImage(homeCategory.getImage());
        }
        if (homeCategory.getCategoryId() != null) {
            toUpdate.setCategoryId(homeCategory.getCategoryId());
        }

        return homeCategoryRepository.save(toUpdate);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory getHomeCategoryById(Long id) throws Exception {
        return homeCategoryRepository.findById(id).orElseThrow(
                () -> new Exception("Cannot find homecategory")
        );
    }
}
