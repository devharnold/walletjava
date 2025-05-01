package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Wallet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.*;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

import com.fintech.fintechapp.payment.MpesaService;
import com.fintech.fintechapp.mapper.WalletRowMapper;



@Repository
public class WalletRepository {
    private static JdbcTemplate jdbcTemplate = null;
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
        String sql = "SELECT * FROM wallets WHERE wallet_id = ?";

        return jdbcTemplate.query(sql, new Object[]{walletId}, rs -> {
            if (rs.next()) {
                Wallet wallet = new Wallet();
                wallet.setWalletId(rs.getInt("wallet_id"));
                wallet.setBalance(rs.getBigDecimal("balance"));
                wallet.setVersion(rs.getInt("version"));
                return Optional.of(wallet);
            }
            return Optional.empty();
        });
    }

    public static int updateWalletBalanceWithVersion(Integer walletId, BigDecimal newBalance, Integer currentVersion) {
        String sql = "UPDATE wallets SET balance = ?, version = version + 1 WHERE wallet_id = ? AND version = ?";
        return jdbcTemplate.update(sql, newBalance, walletId, currentVersion);
    }

    public Optional<BigDecimal> findWalletBalance(BigDecimal balance) {
        String query = "SELECT balance FROM wallets WHERE wallet_id = ?";
        BigDecimal amount = jdbcTemplate.queryForObject(query, BigDecimal.class, balance);
        return Optional.ofNullable(balance);
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
