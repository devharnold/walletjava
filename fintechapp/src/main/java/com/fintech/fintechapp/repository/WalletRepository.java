package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Wallet;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.*;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import com.fintech.fintechapp.payment.MpesaService;
import com.fintech.fintechapp.mapper.WalletRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static java.math.BigDecimal.*;


@Repository
public class WalletRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Wallet> rowMapper = new WalletRowMapper();

    @Autowired
    public MpesaService mpesaService;
    public WalletRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Wallet> WalletRowMapper = (rs, rowNum) -> {
        Wallet wallet = new Wallet();
        wallet.setWalletId(rs.getInt("wallet_id"));
        wallet.setBalance(rs.getBigDecimal("balance"));
        return wallet;
    };
    public RowMapper<Wallet> getWalletRowMapper() {
        return WalletRowMapper;
    }
    public void setWalletRowMapper(RowMapper<Wallet> walletRowMapper) {
        WalletRowMapper = walletRowMapper;
    }

    public Wallet createWallet(Wallet wallet) {
        String insertQuery = "INSERT INTO wallets (wallet_id, user_id, balance, createdAt) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery, wallet.getWalletId());

        // Get the generated id after insert
        String selectQuery = "SELECT id FROM wallets WHERE wallet_id = ?";
        Integer id = jdbcTemplate.queryForObject(selectQuery, Integer.class, wallet.getWalletId());
        wallet.setWalletId(wallet.getWalletId());
        return wallet;
    }

    public Optional<Wallet> findByWalletId(Integer walletId) {
        String query = "SELECT * FROM wallets WHERE wallet_id = ?";
        List<Wallet> wallets = jdbcTemplate.query(query, WalletRowMapper, walletId);
        return wallets.isEmpty() ? Optional.empty() : Optional.of(wallets.get(0));
    }

    public Optional<Wallet> findWalletBalance(BigDecimal balance) {
        String query = "SELECT balance FROM wallets WHERE wallet_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(query, BigDecimal.class, balance);
        return Optional.ofNullable(balance);
    }

    @Transactional
    public void transfer(Integer fromWalletId, Integer toWalletId, BigDecimal amount, LocalDateTime createdAt) {
        Optional<Wallet> fromOpt = findByWalletId(fromWalletId);
        Optional<Wallet> toOpt = findByWalletId(toWalletId);

        if (fromOpt.isEmpty() || toOpt.isEmpty()) {
            throw new IllegalArgumentException("One or both wallets not found.");
        }

        Wallet fromWallet = fromOpt.get();
        Wallet toWallet = toOpt.get();

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in sender's wallet.");
        }

        // Debit sender
        BigDecimal newSenderBalance = fromWallet.getBalance().subtract(amount);
        updateSenderWalletBalance(fromWalletId, newSenderBalance);

        // Credit receiver
        BigDecimal newReceiverBalance = toWallet.getBalance().add(amount);
        updateReceiverWalletBalance(toWalletId, newReceiverBalance);

        // Log transaction
        String logQuery = "INSERT INTO wallet_transfers (from_wallet_id, to_wallet_id, amount, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(logQuery, fromWalletId, toWalletId, amount, Timestamp.valueOf(createdAt));
    }

    private void updateSenderWalletBalance(Integer fromWalletId, BigDecimal newSenderBalance) {
        String query = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        jdbcTemplate.update(query, newSenderBalance, fromWalletId);
    }

    private void updateReceiverWalletBalance(Integer toWalletId, BigDecimal newReceiverBalance) {
        String query = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        jdbcTemplate.update(query, newReceiverBalance, toWalletId);
    }

    @Transactional
    public void fundWallet(String shortCode, String commandID, String amountStr, String MSISDN, String billRefNumber, Integer walletId) throws IOException {
        System.out.println("Funding wallet.../");

        // Simulate mpesa payment
        mpesaService.C2BSimulation(shortCode, commandID, amountStr, MSISDN, billRefNumber);

        // convert amount string to number
        double amount = Double.parseDouble(amountStr);

        // Optional: get current balance first then add it
        String selectQuery = "SELECT balance FROM wallets WHERE wallet_id = ?";
        Double currentBalance = jdbcTemplate.queryForObject(selectQuery, new Object[]{walletId}, Double.class);

        double newBalance = currentBalance + amount;

        String updateQuery = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        jdbcTemplate.update(updateQuery, newBalance, walletId);
    }

    @Transactional
    public void updateWalletBalanceAfterMpesaCallback(Integer walletId, String amountStr) {
        double amount = Double.parseDouble(amountStr);

        String selectQuery = "SELECT balance FROM wallets WHERE wallet_id = ?";
        Double currentBalance = jdbcTemplate.queryForObject(selectQuery, new Object[]{walletId}, Double.class);

        double newBalance = currentBalance + amount;

        String updateQuery = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        jdbcTemplate.update(updateQuery, newBalance, walletId);
    }
}
