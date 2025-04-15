package com.fintech.fintechapp.dao;

import com.fintech.fintechapp.model.TransactionLog;

import java.sql.SQLException;
import java.util.List;

public interface TransactionlogDAO {
    void save(TransactionLog log) throws SQLException;
    List<TransactionLog> findByUserId(Long userId) throws SQLException;
}