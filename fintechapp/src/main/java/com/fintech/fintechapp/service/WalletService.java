package com.fintech.fintechapp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.payment.MpesaService;
import com.fintech.fintechapp.repository.WalletRepository;
import com.fintech.fintechapp.repository.TransactionRepository;


@Service // Annotation:: Wallet service, handles the business logic
public class WalletService {

    private final WalletRepository walletRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private MpesaService mpesaService;

    // Automatically injects the WalletRepository and TransactionRepository
    public WalletService(WalletRepository walletRepository, JdbcTemplate jdbcTemplate) {
        this.walletRepository = walletRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Wallet createWallet(Wallet wallet) {
        return walletRepository.createWallet(wallet);
    }

    public Wallet getWalletById(Integer id) {
        return walletRepository.findByWalletId(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Optional<Wallet> getWalletBalance(BigDecimal amount) {
        return walletRepository.findWalletBalance(amount);
    }

    @Transactional
    public void transfer(Integer fromWalletId, Integer toWalletId, BigDecimal amount, LocalDateTime createdAt) {
        Optional<Wallet> fromOpt = walletRepository.findByWalletId(fromWalletId);
        Optional<Wallet> toOpt = walletRepository.findByWalletId(toWalletId);

        if (fromOpt.isEmpty() || toOpt.isEmpty()) {
            throw new IllegalArgumentException("One or both wallets not found.");
        }

        Wallet fromWallet = fromOpt.get();
        Wallet toWallet = toOpt.get();

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in sender's wallet.");
        }

        int fromVersion = fromWallet.getVersion();
        int toVersion = toWallet.getVersion();

        // Attempt to debit the sender with optimistic locking
        int rowsUpdatedFrom = walletRepository.updateWalletBalanceWithVersion(
                fromWalletId,
                fromWallet.getBalance().subtract(amount),
                fromVersion
        );

        if (rowsUpdatedFrom == 0) {
            throw new IllegalStateException("Wallet already updated. Try again");
        }

        // Credit receiver with optimistic lock
        int rowsUpdatedTo = walletRepository.updateWalletBalanceWithVersion(
                toWalletId,
                toWallet.getBalance().add(amount),
                toVersion
        );

        if (rowsUpdatedTo == 0) {
            throw new IllegalStateException("Wallet already updated. Try again");
        }

        // Log transaction
        String logQuery = "INSERT INTO transactions (from_wallet_id, to_wallet_id, amount, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(logQuery, fromWalletId, toWalletId, amount, Timestamp.valueOf(createdAt));
    }

    @Transactional
    public void creditWallet(String shortCode, String commandID, String amountStr, String MSISDN, String billRefNumber, Integer walletId) throws IOException {
        System.out.println("Funding wallet...");

        // Simulate payment
        mpesaService.C2BSimulation(shortCode, commandID, amountStr, MSISDN, billRefNumber);

        BigDecimal amount = new BigDecimal(amountStr);

        Wallet wallet = walletRepository.findByWalletId(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        BigDecimal newBalance = wallet.getBalance().add(amount);
        int updatedRows = WalletRepository.updateWalletBalanceWithVersion(walletId, newBalance, wallet.getVersion());

        if (updatedRows == 0) {
            throw new IllegalStateException("Wallet was concurrently updated. Try again.");
        }

        // Log transaction: Since you're funding the wallet, there's no 'to_wallet_id'
        String logQuery = "INSERT INTO wallet_transfers (from_wallet_id, amount, created_at, mobile_number) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(logQuery, null, amount, Timestamp.valueOf(LocalDateTime.now()), MSISDN); // MSISDN is the recipient (mobile number)
    }

    @Transactional
    public void debitWallet(String initiatorName, String securityCredential, String commandID,
                            String amountStr, String partyA, String partyB, String remarks,
                            String queueTimeOutURL, String resultURL, String occasion, Integer walletId) throws IOException {

        System.out.println("Processing withdrawal request...");

        BigDecimal amount = new BigDecimal(amountStr);

        Wallet wallet = walletRepository.findByWalletId(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        try {
            mpesaService.B2CRequest(initiatorName, securityCredential, commandID,
                    amountStr, partyA, partyB, remarks,
                    queueTimeOutURL, resultURL, occasion);

            BigDecimal newBalance = wallet.getBalance().subtract(amount);
            int updatedRows = WalletRepository.updateWalletBalanceWithVersion(walletId, newBalance, wallet.getVersion());

            if (updatedRows == 0) {
                throw new IllegalStateException("Wallet was concurrently updated. Try again.");
            }

            // Log transaction: Since you're withdrawing to a mobile number, no "to_wallet_id"
            String logQuery = "INSERT INTO wallet_transfers (from_wallet_id, amount, created_at, mobile_number) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(logQuery, walletId, amount, Timestamp.valueOf(LocalDateTime.now()), partyB); // partyB is the mobile number

        } catch (Exception e) {
            throw new IOException("M-Pesa withdrawal failed. Wallet not debited.", e);
        }
    }

}
