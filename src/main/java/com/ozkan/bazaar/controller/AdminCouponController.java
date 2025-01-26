package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.Coupon;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.service.ICartService;
import com.ozkan.bazaar.service.ICouponService;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class AdminCouponController {

    private final ICouponService couponService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String apply,
            @RequestParam String code,
            @RequestParam double orderValue
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;

        if (apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(
            @RequestBody Coupon coupon
    ){
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }

    @PostMapping("/admin/delete/{couponId}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long couponId) throws Exception {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok("Coupon deleted");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        return ResponseEntity.ok(couponService.findAllCoupons());
    }




}
