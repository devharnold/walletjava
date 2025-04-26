package com.fintech.fintechapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.fintech.fintechapp.model.User;
import com.fintech.fintechapp.model.Wallet;
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

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public Optional<User> getUserByName(String name) {
        return Optional.ofNullable(userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found!")));
    }

    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public Optional<Wallet> getWalletById(Integer id) {
        return Optional.ofNullable(walletRepository.findByWalletId(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found!")));
    }

    public Optional<User> getUserByUserEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

}