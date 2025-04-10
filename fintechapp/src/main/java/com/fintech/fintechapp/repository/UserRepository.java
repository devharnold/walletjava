package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.User;

import net.bytebuddy.implementation.FieldAccessor.FieldLocation.Prepared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class UserRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/my_db";
    private static final String USERNAME = "my_username";
    private static final String PASSWORD = "my_pass";

    public User createUser(User user) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO users()";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, user.getUser());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByUserId(Integer userId) {
        User user = null;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    user = new User(rs.getInt("user_id"), rs.getInt);
                }
            }
        }
    }
}
