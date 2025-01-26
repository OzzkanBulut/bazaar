package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.Coupon;
import com.ozkan.bazaar.model.User;

import java.util.List;

public interface ICouponService {

    Cart applyCoupon(String couponCode, double orderValue, User user) throws Exception;
    Cart removeCoupon(String couponCode, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon createCoupon(Coupon coupon);
    List<Coupon> findAllCoupons();
    void deleteCoupon(Long id) throws Exception;

}
