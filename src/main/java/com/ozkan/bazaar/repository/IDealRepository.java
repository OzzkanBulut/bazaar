package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDealRepository extends JpaRepository<Deal, Long> {

}
