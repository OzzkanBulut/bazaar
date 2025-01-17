package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {

    public Category findByCategoryId(String categoryId);

}
