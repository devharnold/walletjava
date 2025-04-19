package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository

public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Row mapper to map the resultSet to an User object
    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setWalletBalance(rs.getBigDecimal("balance"));
        return user;
    };

    public User createUser(User user) {
        String query = "INSERT INTO users (first_name, last_name, user_email, phone, currency) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getUserId());

        //Get the generated ID after insert
        String selectQuery = "SELECT id FROM users WHERE user_id = ?";
        Integer id = jdbcTemplate.queryForObject(selectQuery, Integer.class, user.getUserId());
        user.setUserId(user.getUserId());
        return user;
    }

    // Find user by user_id
    public Optional<User> findByUserId(Integer userId) {
        String query = "SELECT wallets FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(query, userRowMapper, userId);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    // Get users balance
    public Optional<BigDecimal> findUserBalance(BigDecimal balance) {
        String query = "SELECT balance FROM wallets WHERE user_id = ?";
        BigDecimal = jdbcTemplate.queryForObject(query, BigDecimal.class, balance);
        return Optional.ofNullable(balance);
    }


    // get user by name
    public Optional<User> findByFirstName(String firstName) {
        String query = "SELECT wallets FROM users WHERE firstName = ?";
        List<User> users = jdbcTemplate.query(query, userRowMapper, firstName);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    // get user by email
    public Optional<User> findByEmail(String email) {
        String query = "SELECT wallets FROM users WHERE user_email = ?";
        List<User> users = jdbcTemplate.query(query, userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}