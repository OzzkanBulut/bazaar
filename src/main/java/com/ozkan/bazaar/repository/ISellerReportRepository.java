package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellerReportRepository extends JpaRepository<SellerReport,Long> {

    SellerReport findBySellerId(Long sellerId);
}
