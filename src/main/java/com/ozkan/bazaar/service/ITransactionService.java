package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Order;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.Transaction;

import java.util.List;

public interface ITransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionsBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
