package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHomeCategoryRepository extends JpaRepository<HomeCategory, Long> {

}
