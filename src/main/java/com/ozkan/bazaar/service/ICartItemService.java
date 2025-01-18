package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.CartItem;

public interface ICartItemService {

    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception;
    void removeCartItem(Long userId, Long id) throws Exception;
    CartItem findCartItemById(Long id) throws Exception;
}
