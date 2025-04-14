package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.Card;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import com.fintech.fintechapp.model.User;

@Repository
public class CardRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Row Mapper to map the result set to a Object
//    private RowMapper<Card> cardRowMapper = (rs, rowNum) -> {
//        Card virtualcard = new Card();
//        virtualcard.setCardNumber(rs.getString("card_number"));
//        return virtualcard;
//    };
    private RowMapper<Card> cardRowMapper = new RowMapper<Card>() {
        @Override
        public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
            Card virtualcard = new Card();
            virtualcard.setCardNumber(rs.getString("card_number"));
        }
    };

    public Card createVirtualcard(Card virtualcard) {
        String query = "INSERT INTO cards (card_number, currency, user_id, balance, wallet_id, created_at)";
        jdbcTemplate.update(query, virtualcard.getCardNumber(), virtualcard.getBalance());

        String selectQuery = "SELECT card_number FROM cards WHERE card_number = ?";
        Integer cardNumber = jdbcTemplate.queryForObject(selectQuery, Integer.class, virtualcard.getCardNumber());
        virtualcard.setCardNumber(cardNumber);
        return virtualcard;
    }

    public Optional<Double> findCardBalance(Integer cardNumber) {
        String query = "SELECT balance FROM cards WHERE cardNumber = ?";
        Double balance = jdbcTemplate.queryForObject(query, Double.class, cardNumber);
        return Optional.ofNullable(balance);
    }

    public Optional<Integer> getCardNumber(Long userId) {
        String query = "SELECT card_number FROM cards WHERE user_id = ?";
        Integer cardNumber = jdbcTemplate.queryForObject(query, Integer.class, cardNumber);
        return Optional.ofNullable(cardNumber);
    }

    public RowMapper<Card> getCardRowMapper() {
        return cardRowMapper;
    }

    public void setCardRowMapper(RowMapper<Card> cardRowMapper) {
        this.cardRowMapper = cardRowMapper;
    }
}