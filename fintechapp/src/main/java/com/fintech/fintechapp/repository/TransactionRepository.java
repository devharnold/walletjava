package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Transaction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.fintech.fintechapp.mapper.TransactionRowMapper;

import java.math.*;
import java.sql.Timestamp;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Transaction> rowMapper = new TransactionRowMapper();

    @Autowired
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Row mapper to map the result set to a Transaction object
    private RowMapper<Transaction> TransactionRowMapper = (rs, rowNum) -> {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        transaction.setTransactionId(rs.getString("transaction_id"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        return transaction;
    };

    // create (initialize ) a transaction
    public Transaction createTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (transaction_id, sender_user_id, receiver_user_id, currency, amount, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, transaction.getTransactionId(), transaction.getAmount(), transaction.getCreatedAt());

        //get the generated id after insert query
        String selectQuery = "SELECT id FROM transactions WHERE transaction_id = ?";
        Long id = jdbcTemplate.queryForObject(selectQuery, Long.class, transaction.getTransactionId());
        transaction.setTransactionId("id");
        return transaction;
    }

    // find transaction by (transaction_id)
    public Optional<Transaction> findByTransactionId(String transactionId) {
        String query = "SELECT * FROM transactions WHERE transaction_id = ?";
        List<Transaction> transactions = jdbcTemplate.query(query, TransactionRowMapper, transactionId);
        return transactions.isEmpty() ? Optional.empty() : Optional.of(transactions.get(0));
    }

    // find transaction by date
    public List<Transaction> findByDateCreated(LocalDateTime createdAt) {
        String query = "SELECT * FROM transactions WHERE created_At = ?";
        return jdbcTemplate.query(query, TransactionRowMapper, createdAt);
    }

    public List<Transaction> findByAmount(BigDecimal amount) {
        String query = "SELECT * FROM transactions WHERE amount = ?";
        return jdbcTemplate.query(query, TransactionRowMapper, amount);
    }



}