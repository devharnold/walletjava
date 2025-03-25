package com.fintech.fintechapp.service;

import java.util.List;
import com.fintech.fintechapp.model.Wallet;
import org.springframework.stereotype.Service;
import com.fintech.fintechapp.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Service // Annotation:: Wallet service, handles the business logic
public class WalletService {
    private final WalletRepository walletRepository;

    @Autowired // Automatically injects the WalletRepository
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
}
