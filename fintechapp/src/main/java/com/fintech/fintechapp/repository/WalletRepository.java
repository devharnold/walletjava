package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Wallet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class WalletRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WalletRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to map the result set to a Wallet object
    private RowMapper<Wallet> walletRowMapper = (rs, rowNum) -> {
        Wallet wallet = new Wallet();
        wallet.setWalletId(rs.getString("wallet_id"));
        wallet.setBalance(rs.getBigDecimal("balance"));
        return wallet;
    };

    // Create wallet
    public Wallet createWallet(Wallet wallet) {
        String query = "INSERT INTO wallets (wallet_id, balance) VALUES (?, ?)";
        jdbcTemplate.update(query, wallet.getWalletId(), wallet.getBalance());
        
        // Get the generated ID after insert
        String selectQuery = "SELECT id FROM wallets WHERE wallet_id = ?";
        Long id = jdbcTemplate.queryForObject(selectQuery, Long.class, wallet.getWalletId());
        wallet.setWalletId(id);
        return wallet;
    }

    // Find wallet by wallet_id
    public Optional<Wallet> findByWalletId(String walletId) {
        String query = "SELECT * FROM wallets WHERE wallet_id = ?";
        List<Wallet> wallets = jdbcTemplate.query(query, walletRowMapper, walletId);
        return wallets.isEmpty() ? Optional.empty() : Optional.of(wallets.get(0));
    }

    // Find wallet balance
    public Optional<Double> findWalletBalance(Long id) {
        String query = "SELECT balance FROM wallets WHERE id = ?";
        Double balance = jdbcTemplate.queryForObject(query, Double.class, id);
        return Optional.ofNullable(balance);
    }

    // Update wallet balance (deposit or withdraw)
    public int updateBalance(Long walletId, BigDecimal balance) {
        String query = "UPDATE wallets SET balance = ? WHERE id = ?";
        return jdbcTemplate.update(query, balance, walletId);
    }
}
