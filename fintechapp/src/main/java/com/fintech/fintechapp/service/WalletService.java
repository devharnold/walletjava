package com.fintech.fintechapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.repository.WalletRepository;
import com.fintech.fintechapp.repository.TransactionRepository;

@Service // Annotation:: Wallet service, handles the business logic
public class WalletService {
    
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Autowired // Automatically injects the WalletRepository and TransactionRepository
    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Optional<Double> getWalletBalance(Long id) {
        return walletRepository.findWalletBalance(id);
    }
    
    public Optional<Wallet> getWalletByWalletId(String walletId) {
        return walletRepository.findByWalletId(walletId);
    }

    @Transactional
    public Transaction transfer(String senderWalletId, String receiverWalletId, BigDecimal amount, String message) {
        Wallet sender = walletRepository.findByWalletId(senderWalletId)
            .orElseThrow(() -> new RuntimeException("Sender wallet not found!"));

        Wallet receiver = walletRepository.findByWalletId(receiverWalletId)
            .orElseThrow(() -> new RuntimeException("Receiver wallet not found!"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        // Debit sender
        sender.setBalance(sender.getBalance().subtract(amount));

        // Credit receiver
        receiver.setBalance(receiver.getBalance().add(amount));

        // Save updated balances
        walletRepository.save(sender);
        walletRepository.save(receiver);

        // Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionRef(UUID.randomUUID().toString());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setMessage(message);

        // Save transaction and return
        return transactionRepository.save(transaction);
    }
}
