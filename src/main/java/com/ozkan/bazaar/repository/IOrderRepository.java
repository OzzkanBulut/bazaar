package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserId(Long userId);
    List<Order> findBySellerId(Long sellerId);
    Order findOrderById(Long id);


}
