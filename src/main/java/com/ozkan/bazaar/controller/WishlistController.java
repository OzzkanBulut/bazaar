package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.model.Wishlist;
import com.ozkan.bazaar.service.IProductService;
import com.ozkan.bazaar.service.IUserService;
import com.ozkan.bazaar.service.IWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final IWishlistService wishlistService;
    private final IUserService userService;
    private final IProductService productService;

    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(
            @RequestBody User user

    ) {
        Wishlist wishlist = wishlistService.createWishlist(user);
        return new ResponseEntity<>(wishlist, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long productId
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        return ResponseEntity.ok(wishlistService.addProductToWishlish(user, product));


    }


}
