package com.fintech.fintechapp.controller;

import java.io.IOException;
import java.util.Optional;
import java.math.BigDecimal;

import com.fintech.fintechapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;


import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;

import com.fintech.fintechapp.service.WalletService;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) {
        Wallet createdWallet = walletService.createWallet(wallet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWallet);
    }

    @GetMapping("/api/wallets/{walletId}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Integer walletId) {
        return walletService.getWalletById(walletId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/wallets/{wallet_id}/balance")
    public ResponseEntity<Wallet> checkBalance(@PathVariable BigDecimal amount) {
        Optional<Wallet> balance = walletService.getWalletBalance(amount);
        return balance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/api/wallets/{wallet_id}/deposit")
    public ResponseEntity<Transaction> depositToWallet(
        String shortCode,
        String commandID,
        String amount,
        String MSISDN,
        String billRefNumber,
        Integer walletId
    ) throws IOException {
        Transaction txn = walletService.creditWallet(shortCode, commandID, amount, MSISDN, billRefNumber, walletId);
        return ResponseEntity.ok(txn);
    }

    @PostMapping("/api/wallets/{wallet_id}/withdraw")
    public ResponseEntity<Transaction> withdrawFromWallet(
            String initiatorName,
            String securityCredential,
            String commandID,
            String amount,
            String partyA,
            String partyB,
            String remarks,
            String queueTimeOutURL,
            String resultURL,
            String occassion
    ) throws IOException {
        Transaction txn = walletService.debitWallet(initiatorName, securityCredential, commandID, amount, partyA, partyB, remarks, queueTimeOutURL, resultURL, occassion);
        return ResponseEntity.ok(txn);
    }
}
