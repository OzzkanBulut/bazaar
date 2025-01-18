package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.CartItem;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.ICartItemRepository;
import com.ozkan.bazaar.service.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final ICartItemRepository cartItemRepository;




    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {

        CartItem item = findCartItemById(id);
        User cartItemUser = item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()+item.getProduct().getSellingPrice());
            item.setSellingPrice(item.getQuantity()+item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }

        throw new Exception("You can't update this item");

    }

    @Override
    public void removeCartItem(Long userId, Long id) throws Exception {
        CartItem item = findCartItemById(id);
        if(item.getUserId().equals(userId)){
            cartItemRepository.delete(item);
        }else{
            throw new Exception("You can't delete this item");
        }
    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id).orElseThrow(
                () -> new Exception("Cart Item not found with id:"+id)
        );
    }
}
