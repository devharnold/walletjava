package com.fintech.fintechapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import javax.validation.constratints.NotNull;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.service.WalletService;
import com.fintech.fintechapp.repository.WalletRepository;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    //private WalletService walletService;
    private WalletRepository walletRepository;
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) {
        Wallet createdWallet = walletRepository.createWallet(wallet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long walletId) {
        Wallet wallet = walletRepository.getWalletById(walletId);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getAllWallets() {
        List<Wallet> wallets = walletService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable Long walletId, @RequestParam double amount) {
        wallet updatedWallet = walletService.deposit(walletId, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable Long walletId, @RequestParam double amount) {
        wallet updatedWallet = walletService.withdraw(walletId, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<Double> checkBalance(@PathVariable Long walletId) {
        Optional<Double> balance = walletService.getWalletBalance(walletId);
        if (balance.isPresent()) {
            return ResponseEntity.ok(balance.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{senderWalletId}/transfer/{receiverWalletId}")
    public ResponseEntity<Transaction> transferFunds(
        @RequestParam String senderWalletId,
        @RequestParam String receiverWalletId,
        @RequestParam Double amount,
        @RequestParam(required = false) String message 
    ) {
        Transaction trsn = walletService.transfer(senderWalletId, receiverWalletId, amount);
        return ResponseEntity.ok(trsn);
    }
    
}
