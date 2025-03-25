package com.fintech.fintechapp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.repository.WalletRepository;

@RestController
public class WalletController {
    private final WalletRepository repository;

    WalletController(WalletRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping("/wallets")
    List<Wallet> all() {
        return repository.findAll();
    }

    // create a new wallet
    @PostMapping("/wallets")
    Wallet newwallet(@RequestBody Wallet newWallet) {
        return repository.save(newWallet);
    }

    // Single item:: get wallet by id
    @GetMapping("/wallets/{id}")
    Wallet one(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new WalletNotFoundException(id));
    }

    // update an existing wallet
    @PutMapping("/wallets/{id}")
    Wallet replaceWallet(@RequestBody Wallet newWallet, @PathVariable Long id) {
        return repository.findById(id)
        .map(wallet -> {
            wallet.setUserId(newWallet.getUserId());
            wallet.setWalletId(newWallet.getWalletId());
            return repository.save(wallet);
        })
        .orElseGet(() -> {
            return repository.save(newWallet);
        });
    }
}
