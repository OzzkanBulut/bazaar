package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart,Long> {

    Cart findByUserId(Long id);


}
