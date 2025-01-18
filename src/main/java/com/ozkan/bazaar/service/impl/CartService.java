package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.CartItem;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.ICartItemRepository;
import com.ozkan.bazaar.repository.ICartRepository;
import com.ozkan.bazaar.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;


    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws Exception {

        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            int totalPrice = quantity * product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());


            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }

        return isPresent;

    }

    @Override
    public Cart findUserCart(User user) throws Exception {
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new HashSet<>());
            cart.setCouponCode("");
            cart.setTotalMrpPrice(0);
            cart.setTotalSellingPrice(0);
            cart.setTotalItem(0);
            cart.setDiscount(0);
        }else{

            int totalPrice = 0;
            int totalDiscountedPrice = 0;
            int totalItem = 0;

            for (CartItem cartItem : cart.getCartItems()) {
                totalPrice += cartItem.getMrpPrice();
                totalDiscountedPrice += cartItem.getSellingPrice();
                totalItem += cartItem.getQuantity();
            }

            cart.setTotalMrpPrice(totalPrice);
            cart.setTotalItem(totalItem);
            cart.setTotalSellingPrice(totalDiscountedPrice);
            cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));

        }
        return cartRepository.save(cart);

    }

    private int calculateDiscountPercentage(int totalPrice, int totalDiscountedPrice) {
        return 0;
    }
}
