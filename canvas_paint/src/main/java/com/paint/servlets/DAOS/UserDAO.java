package com.paint.servlets.DAOS;


import com.paint.servlets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<User> rowMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("name"),
                rs.getString("user"),
                rs.getString("password")
        );
    }

    public boolean checkUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE user = ? AND password = ?";
        List<User> users = jdbcTemplate.query(sql, rowMapper(), username, password);
        if (!users.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean userExists(String username) {
        String sql = "SELECT count(*) FROM users WHERE user = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public String getName(String username) {
        String sql = "SELECT name FROM users WHERE user = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, username);
        } catch (EmptyResultDataAccessException e) {
            return "Usuario Desconocido";
        }
    }

    public void registerUser(User user) {
        String sql="INSERT INTO users (user,name,password) VALUES(?,?,?)";

        jdbcTemplate.update(sql,user.getUser(),user.getName(),user.getPassword());
    }
}