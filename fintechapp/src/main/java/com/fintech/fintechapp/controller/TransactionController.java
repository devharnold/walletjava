package com.fintech.fintechapp.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.service.TransactionService;
import com.fintech.fintechapp.repository.TransactionRepository;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/{transactionId")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{transaction}")
    public ResponseEntity<Transaction> getTransactionByDate(@PathVariable LocalDateTime createdAt) {
        Transaction transaction = transactionService.getTransactionByDateCreated(LocalDateTime createdAt);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{transaction")
    public ResponseEntity<Transaction> getTransactionByAmount(@PathVariable Double amount) {
        Transaction transaction = transactionService.getTransactionByAmount(Double amount);
        return ResponseEntity.ok(transaction);
    }
}
