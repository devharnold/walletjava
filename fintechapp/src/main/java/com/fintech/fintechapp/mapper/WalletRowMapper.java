package com.fintech.fintechapp.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.fintech.fintechapp.model.Wallet;

import org.springframework.jdbc.core.RowMapper;

public class WalletRowMapper implements RowMapper<Wallet> {
    @Override
    public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {
        Wallet wallet = new Wallet();
        wallet.setWalletId(rs.getInt("wallet_id"));
        wallet.setUserId((rs.getLong("user_id")));
        wallet.setBalance(rs.getBigDecimal("balance"));
        wallet.setCurrency(rs.getString("currency"));
        return wallet;
    }
}