package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findBySellerId(Long sellerId);
}
