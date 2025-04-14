package com.fintech.fintechapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.fintechapp.model.User;
import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.repository.UserRepository;
import com.fintech.fintechapp.repository.WalletRepository;
import com.fintech.fintechapp.repository.TransactionRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public UserService(UserRepository userRepository, WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByFirstName(String firstName) {
        return userRepository.findByFirstName()
            .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public Optional<Wallet> getWalletById(Long id) {
        return walletRepository.findByWalletId(id)
            .orElseThrow(() -> new RuntimeException("Wallet not found!"));
    }

}