package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICouponRepository extends JpaRepository<Coupon,Long> {

    Coupon findByCode(String code);

}
