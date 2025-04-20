package com.fintech.fintechapp.controller;

import java.io.IOException;
import java.util.Optional;

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

    @GetMapping("/api/wallets/{wallet_id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Integer walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return ResponseEntity.ok(wallet);
    }


    @GetMapping("/api/wallets/{wallet_id}/balance")
    public ResponseEntity<Double> checkBalance(@PathVariable Long walletId) {
        Optional<Double> balance = walletService.getWalletBalance(walletId);
        return balance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/api/wallets/{wallet_id}/deposit")
    public ResponseEntity<Transaction> depositToWallet(
        String shortCode,
        String commandID,
        String amount,
        String MSISDN,
        String billRefNumber
    ) throws IOException {
        Transaction txn = walletService.fundWallet(shortCode, commandID, amount, MSISDN, billRefNumber);
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
