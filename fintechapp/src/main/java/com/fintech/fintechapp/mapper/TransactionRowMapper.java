package com.fintech.fintechapp.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import com.fintech.fintechapp.model.Transaction;

import org.springframework.jdbc.core.RowMapper;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getString("transaction_id"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        return transaction;
    }
}