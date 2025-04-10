package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.sql.DriverManager;


@Repository
public class TransactionRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/my_db";
    private static final String USERNAME = "my_username";
    private static final String PASSWORD = "my_pass";

    public Transaction createTransaction(Transaction transaction) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO transactions(transaction_id, sender_id, amount, currency, created_at) VALUES (?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, transaction.getTransactionId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    transaction.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM transactions";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Transaction transaction = new Transaction(rs.getLong("id"), rs.getString("name"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public Transaction getTransactionByTransactionId(Long id) {
        Transaction transaction = null;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM transactions WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    transaction = new Transaction(rs.getLong("id"), rs.getString(0))
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    
}