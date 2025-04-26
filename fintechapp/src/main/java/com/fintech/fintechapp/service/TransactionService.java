package com.fintech.fintechapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.repository.TransactionRepository;
import com.fintech.fintechapp.repository.WalletRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    // Create a new transaction
    public Transaction createTransaction(Transaction transaction) {
        // You can add validation or wallet balance checks here if needed
        return transactionRepository.createTransaction(transaction);
    }

    // Fetch a transaction by its unique ID
    public Optional<Transaction> getTransactionById(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    // Fetch all transactions created at a specific timestamp
    public List<Transaction> getTransactionsByDateCreated(LocalDateTime createdAt) {
        return transactionRepository.findByDateCreated(createdAt);
    }

    // Fetch all transactions with a given amount
    public List<Transaction> getTransactionsByAmount(BigDecimal amount) {
        return transactionRepository.findByAmount(amount);
    }


}
