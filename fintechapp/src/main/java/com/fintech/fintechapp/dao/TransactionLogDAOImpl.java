package com.fintech.fintechapp.dao;

import java.sql.*;
import java.util.*;

import com.fintech.fintechapp.model.TransactionLog;
import com.fintech.fintechapp.dao.TransactionlogDAO;


public class TransactionLogDAOImpl implements TransactionlogDAO {
    private final Connection connection;

    public TransactionLogDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(TransactionLog log) throws SQLException {
        String sql = "INSERT INTO transaction_logs (user_id, amount, description, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, log.getId());
            stmt.setBigDecimal(2, log.getAmount());
            stmt.setString(3, log.getDescription());
            stmt.setString(4, log.getStatus());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<TransactionLog> findByUserId(Long userId) throws SQLException {
        List<TransactionLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM transaction_logs WHERE user_id = ? ORDER BY created_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TransactionLog log = new TransactionLog();
                log.setId(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setAmount(rs.getBigDecimal("amount"));
                log.setStatus(rs.getString("status"));
                logs.add(log);
            }
        }
        return logs;
    }
}
