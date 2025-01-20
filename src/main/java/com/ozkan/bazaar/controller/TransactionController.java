package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.Transaction;
import com.ozkan.bazaar.service.ISellerService;
import com.ozkan.bazaar.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ITransactionService transactionService;
    private final ISellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionsBySeller(
            @RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

}
