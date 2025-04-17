package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Wallet;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.*;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import com.fintech.fintechapp.Mpesa;


import java.time.LocalDateTime;


@Repository
public class WalletRepository {

    private final Connection connection;
    private final RowMapper<Wallet> rowMapper = new WalletRowMapper();

    @Autowired
    public WalletRepository(Connection connection) {
        this.connection = connection;
    }

    public Wallet createWallet(Wallet wallet) {
        String insertQuery = "INSERT INTO wallets (wallet_id, user_id, balance) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, wallet.getWalletId());
            stmt.setLong(2, wallet.getUserId());
            stmt.setBigDecimal(3, wallet.getBalance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fetch ID or additional data if needed
        String selectQuery = "SELECT wallet_id, user_id, balance FROM wallets WHERE wallet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setInt(1, wallet.getWalletId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Optional<Wallet> findByWalletId(Integer walletId) {
        String sql = "SELECT * FROM wallets WHERE wallet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, walletId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Wallet> findWalletBalance(BigDecimal balance) {
        String sql = "SELECT balance FROM wallets WHERE wallet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, balance);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void depositFromMpesa(Integer toWalletId, BigDecimal amount, Integer phoneNumber) {
        try {
            connection.setAutoCommit(false);

            Optional<Wallet> toOpt = findByWalletId(toWalletId);

            if (toOpt.isEmpty()) {
                throw new IllegalArgumentException("Wallet not found");
            }

            Wallet toWallet = toOpt.get();
            // credit wallet


        }
    }

    public void transfer(Integer fromWalletId, Integer toWalletId, BigDecimal amount, LocalDateTime createdAt) {
        try {
            connection.setAutoCommit(false); // Begin transaction

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
            updateSenderWalletBalance(Integer.valueOf(fromWalletId), newSenderBalance);

            // Credit receiver
            BigDecimal newReceiverBalance = toWallet.getBalance().add(amount);
            updateReceiverWalletBalance(Integer.valueOf(toWalletId), newReceiverBalance);

            // Optionally log the transaction
            String logQuery = "INSERT INTO wallet_transfers (from_wallet_id, to_wallet_id, amount, created_at) VALUES (?, ?, ?, ?)";
            try (PreparedStatement logStmt = connection.prepareStatement(logQuery)) {
                logStmt.setString(1, fromWalletId);
                logStmt.setString(2, toWalletId);
                logStmt.setBigDecimal(3, amount);
                logStmt.setTimestamp(4, Timestamp.valueOf(createdAt));
                logStmt.executeUpdate();
            }

            connection.commit(); // Commit transaction
        } catch (SQLException | IllegalArgumentException e) {
            try {
                connection.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSenderWalletBalance(Integer fromWalletId, BigDecimal newSenderBalance) {
        String updateQuery = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setBigDecimal(1, newSenderBalance);
            stmt.setInt(2, fromWalletId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateReceiverWalletBalance(Integer toWalletId, BigDecimal newReceiverBalance) {
        String updateQuery = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setBigDecimal(1, newReceiverBalance);
            stmt.setInt(2, toWalletId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



