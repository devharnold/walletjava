package com.fintech.fintechapp.dao;

import com.fintech.fintechapp.model.Wallet;

import java.sql.SQLException;
import java.util.List;

public interface WalletDAO {
    void save(Wallet wallet) throws SQLException;

    static List<Wallet> findByWalletId(Integer walletId) throws SQLException {
        return null;
    }
}