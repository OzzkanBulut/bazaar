package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWishListRepository extends JpaRepository<Wishlist, Long> {

    Wishlist findByUser(Long userId);

}
