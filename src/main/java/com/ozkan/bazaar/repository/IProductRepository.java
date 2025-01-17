package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findBySellerId(Long sellerId);

    @Query("SELECT p FROM Product p WHERE (:query IS NULL OR LOWER(p.title)" +
            "LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR (:query IS NULL OR LOWER(p.category.name)" +
            "LIKE LOWER(CONCAT('%', :query, '%'))))")
    List<Product> searchProduct(@Param("query") String query);




}
