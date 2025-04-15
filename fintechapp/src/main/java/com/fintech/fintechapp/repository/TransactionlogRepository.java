package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.TransactionLog;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.*;
import java.util.*;
import java.time.LocalDateTime;

@Repository
public class TransactionlogRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TransactionLog> transactionLogRowMapper = (rs, rowNum) -> {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setId(rs.getInt("id"));
        transactionLog.setTransactionRef(rs.getString("transactionRef"));
        transactionLog.setWalletId(rs.getInt("walletId"));
        transactionLog.setType(rs.getString("type"));
        transactionLog.setAmount(rs.getBigDecimal("amount"));
    };

    public TransactionLog createTransactionLog(TransactionLog transactionLog) {

    }
}