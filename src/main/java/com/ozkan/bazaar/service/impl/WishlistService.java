package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.model.Wishlist;
import com.ozkan.bazaar.repository.IWishListRepository;
import com.ozkan.bazaar.service.IWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService implements IWishlistService {

    private final IWishListRepository wishListRepository;

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishListRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist = wishListRepository.findByUser(user.getId());
        if (wishlist == null) {
            wishlist = createWishlist(user);
        }

        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlish(User user, Product product) {
        Wishlist wishlist = getWishlistByUserId(user);
        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
        } else {
            wishlist.getProducts().add(product);
        }
        return wishListRepository.save(wishlist);
    }
}
