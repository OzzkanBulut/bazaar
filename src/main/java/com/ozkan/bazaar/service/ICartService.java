package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.CartItem;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.User;

public interface ICartService {

    public CartItem addCartItem(User user,Product product,String size,int quantity) throws Exception;

    public Cart findUserCart(User user) throws Exception;
}
