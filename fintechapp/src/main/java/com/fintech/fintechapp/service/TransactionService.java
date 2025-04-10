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
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction Not Found"));
    }

    public Optional<Double> getTransactionAmount(Long id) {
        return transactionRepository.findTransactionAmount(id);
    }

    
}