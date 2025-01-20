package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.model.Wishlist;

public interface IWishlistService {

    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlish(User user, Product product);

}
