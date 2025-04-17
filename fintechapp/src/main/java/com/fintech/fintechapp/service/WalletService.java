package com.fintech.fintechapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

    public Wallet getWalletById(Integer id) {
        return walletRepository.findByWalletId(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Optional<Double> getWalletBalance(Long id) {
        return walletRepository.findWalletBalance(id);
    }

    public Optional<Wallet> getWalletByWalletId(Integer walletId) {
        return walletRepository.findByWalletId(Integer walletId);
    }

    public Optional<Wallet> transferFunds(Integer fromWalletId, Integer toWalletId, BigDecimal amount, LocalDateTime createdAt) {
        return walletRepository.transfer(Integer fromWalletId, Integer toWalletId, amount, createdAt);
    }
}