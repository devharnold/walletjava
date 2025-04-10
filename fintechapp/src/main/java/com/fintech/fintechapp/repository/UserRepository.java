package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.*;

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
        user.setId(rs.setLong("id"));
        user.setUserId(rs.getString("user_id"));
        user.setBalance(rs.getBigDecimal("balance"));
        return user;
    }

    public User createUser(User user) {
        String query = "INSERT INTO users (first_name, last_name, user_email, phone, currency) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getUserId());

        //Get the generates ID after insert
        String selectQuery = "SELECT id FROM users WHERE user_id = ?";
        Long id = jdbcTemplate.queryForObject(selectQuery, Long.class, user.getUserId());
        user.setId(id);
        return user;
    }

    // Find user by user_id
    public Optional<User> findByUserId(Integer userId) {
        String query = "SELECT wallets FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(query, userRowMapper, userId);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    // Get users balance
    public Optional<Double> findUserBalance(Long id) {
        String query = "SELECT balance FROM wallets WHERE user_id = ?";
        Double balance = jdbcTemplate.queryForObject(query, Double.class, id);
        return Optional.ofNullable(balance);
    }

    // get all users
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, userRowMapper);
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
