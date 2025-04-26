package com.fintech.fintechapp.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.fintech.fintechapp.model.User;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        return user;
    }
}