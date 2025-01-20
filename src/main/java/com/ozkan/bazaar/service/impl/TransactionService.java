package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Order;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.Transaction;
import com.ozkan.bazaar.repository.ISellerRepository;
import com.ozkan.bazaar.repository.ITransactionRepository;
import com.ozkan.bazaar.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final ISellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
