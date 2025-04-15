package com.fintech.fintechapp.dao;

import java.sql.*;
import java.util.*;
import java.math.*;
import java.time.LocalDateTime;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.dao.WalletDAO;
import org.springframework.transaction.annotation.Transactional;

public class WalletDAOImpl implements WalletDAO {
    private final Connection connection;

    public WalletDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Wallet findByWalletId(int walletId) throws SQLException {
        String query = "SELECT * FROM wallets WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, walletId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Wallet(
                        rs.getInt("id"),
                        rs.getBigDecimal("balance"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            } else {
                throw new SQLException("Wallet with ID " + walletId + " not found.");
            }
        }
    }

    @Override
    public void updateWalletBalance(int walletId, BigDecimal newBalance) throws SQLException {
        String update = "UPDATE wallets SET balance = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(update)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, walletId);
            stmt.executeUpdate();
        }
    }

    public void transfer(int fromWalletId, int toWalletId, BigDecimal amount, LocalDateTime createdAt) throws Exception {
        try {
            connection.setAutoCommit(false);

            Wallet fromWallet = findByWalletId(fromWalletId);
            Wallet toWallet = findByWalletId(toWalletId);

            if (fromWallet.getBalance().compareTo(amount) < 0) {
                throw new Exception("Insufficient funds in source wallet");
            }

            BigDecimal newFromBalance = fromWallet.getBalance().subtract(amount);
            BigDecimal newToBalance = toWallet.getBalance().add(amount);

            updateWalletBalance(fromWalletId, newFromBalance);
            updateWalletBalance(toWalletId, newToBalance);

            // Optional: log the transfer here if needed

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
