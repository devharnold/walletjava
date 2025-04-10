package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.VirtualCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.sql.DriverManager;


@Repository
public class VirtualCardRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/my_db";
    private static final String USERNAME = "my_username";
    private static final String PASSWORD = "my_pass";

    public VirtualCard createVirtualCard(VirtualCard virtualcard) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO virtualcards(cardNumber, user_id, )";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, virtualcard.getVirtualCardId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    virtualcard.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return virtualcard;
    }

    public List<VirtualCard> getAllVirtualCards() {
        List<VirtualCard> virtualCards = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM virtualcards";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    VirtualCard virtualCard = new VirtualCard(rs.getLong("id"), rs.getString("name"));
                    virtualCards.add(virtualCard);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return virtualCards;
    }

    public VirtualCard getVirtualCardByVirtualCardId(Long id) {
        VirtualCard virtualcard = null;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM virtualcards WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    virtualcard = new VirtualCard(rs.getLong("id"), rs.getString(0))
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return virtualcard;
    }
}