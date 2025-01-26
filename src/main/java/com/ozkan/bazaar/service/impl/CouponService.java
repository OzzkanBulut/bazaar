package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.Coupon;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.ICartRepository;
import com.ozkan.bazaar.repository.ICouponRepository;
import com.ozkan.bazaar.repository.IUserRepository;
import com.ozkan.bazaar.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {

    private final ICouponRepository couponRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;

    @Override
    public Cart applyCoupon(String couponCode, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(couponCode);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null) {
            throw new Exception("Coupon not valid");
        }

        if (user.getUsedCoupons().contains(coupon)) {
            throw new Exception("Coupon already used");
        }

        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new Exception("Valid for minimum order value" + coupon.getMinimumOrderValue());
        }
        if (coupon.isActive() &&
                LocalDate.now().isAfter(coupon.getValidityStartDate())
                && LocalDate.now().isBefore(coupon.getValidityEndDate())) {

            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice()* coupon.getDiscountPercentage())/100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(couponCode);
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String couponCode, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(couponCode);
        if(coupon == null){
            throw new Exception("Coupon not found");
        }

        Cart cart = cartRepository.findByUserId(user.getId());

        double discountedPrice = (cart.getTotalSellingPrice()* coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(
                () -> new Exception("Coupon not found")
        );
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        couponRepository.delete(findCouponById(id));
    }
}
