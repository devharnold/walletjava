package com.fintech.fintechapp.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.model.Wallet;
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

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.createTransaction(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findByTransactionId(id)
            .orElseThrow(() -> new RuntimeException("Transaction Not Found"));
    }

    public List<Transaction> getTransactionByDateCreated(LocalDateTime createdAt) {
        return transactionRepository.findByDateCreated(createdAt);
    }

    public List<Transaction> getTransactionByAmount(Double amount) {
        return transactionRepository.findByAmount(amount);
    }
    
}