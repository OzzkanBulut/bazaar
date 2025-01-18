package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.CartItem;
import com.ozkan.bazaar.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Long> {

    public CartItem findByCartAndProductAndSize(Cart cart, Product product,String size);
    CartItem findCartItemById(Long id);
}
