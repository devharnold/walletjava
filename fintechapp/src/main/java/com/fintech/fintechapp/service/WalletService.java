package com.fintech.fintechapp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.repository.WalletRepository;
import com.fintech.fintechapp.repository.TransactionRepository;
import com.fintech.fintechapp.payment.MpesaService;

@Service // Annotation:: Wallet service, handles the business logic
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    private MpesaService mpesaService;
    // Automatically injects the WalletRepository and TransactionRepository
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

    @Transactional
    public Optional<Wallet> transferFunds(Integer fromWalletId, Integer toWalletId, BigDecimal amount, LocalDateTime createdAt) {
        return walletRepository.transfer(Integer fromWalletId, Integer toWalletId, amount, createdAt);
    }

    @Transactional
    public Transaction fundWallet(String shortCode, String commandID, String amount, String MSISDN, String billRefNumber) throws IOException {
        System.out.println("Funding wallet.../");
        mpesaService.C2BSimulation(shortCode, commandID, amount, MSISDN, billRefNumber);
        return null;
    }

    @Transactional
    public Transaction debitWallet(String initiatorName, String securityCredential,String commandID, String  amount, String partyA,String partyB, String remarks, String queueTimeOutURL, String resultURL, String occassion) throws IOException {
        System.out.println("Transferring funds from wallet.../");
        mpesaService.B2CRequest(initiatorName, securityCredential, commandID, amount, partyA, partyB, remarks, queueTimeOutURL, resultURL, occassion);
        return null;
    }
}