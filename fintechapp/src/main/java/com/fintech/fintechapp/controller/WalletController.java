package com.fintech.fintechapp.controller;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import javax.xml.validation.

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

    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return ResponseEntity.ok(wallet);
    }


    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable Long walletId, @RequestParam double amount) {
        wallet updatedWallet = walletService.deposit(walletId, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable Integer walletId, @RequestParam double amount) {
        wallet updatedWallet = walletService.withdraw(walletId, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @GetMapping("{walletId}/fetch")
    public ResponseEntity<Wallet> fetch(@PathVariable Integer)

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<Double> checkBalance(@PathVariable Long walletId) {
        Optional<Double> balance = walletService.getWalletBalance(walletId);
        return balance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transactFunds(
        @RequestParam Integer senderWalletId,
        @RequestParam Integer receiverWalletId,
        @RequestParam BigDecimal amount,
        @RequestParam(required = false) String message
    ) {
        Transaction txn = walletService.transferFunds(senderWalletId, receiverWalletId, amount);
        return ResponseEntity.ok(txn);
    }

}
