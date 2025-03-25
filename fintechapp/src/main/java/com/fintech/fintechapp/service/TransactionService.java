package com.fintech.fintechapp.service;

import java.util.List;
import com.fintech.fintechapp.model.Transaction;
import org.springframework.stereotype.Service;
import com.fintech.fintechapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction not found!"));

    }
}