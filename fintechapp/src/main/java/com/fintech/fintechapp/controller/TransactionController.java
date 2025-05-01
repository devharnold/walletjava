package com.fintech.fintechapp.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/api/transactions/make-transaction")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/api/transactions/by-transaction_id")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String transactionId) {
        return transactionService.getTransactionById(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/transactions/by-date")
    public ResponseEntity<Transaction> getTransactionByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt) {
        List<Transaction> transactions = transactionService.getTransactionsByDateCreated(createdAt); //List<Transaction> Lists all transactions that took place during that specific time

        return transactions.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/transactions/amount/{amount}")
    public ResponseEntity<List<Transaction>> getTransactionByAmount(@PathVariable BigDecimal amount) {
        List<Transaction> transactions = transactionService.getTransactionsByAmount(amount);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transactions);
        }
    }
}
