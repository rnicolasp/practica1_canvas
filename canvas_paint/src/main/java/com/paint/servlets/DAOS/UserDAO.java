package com.paint.servlets.DAOS;


import com.paint.servlets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static boolean userExists(String username) {

        return false;
    }

    public static String getName(String username) {
        return "";
    }

    public void registerUser(User user) {
        String sql="INSERT INTO users (user,name,password) VALUES(?,?,?)";

        jdbcTemplate.update(sql,user.getUser(),user.getName(),user.getPassword());
    }
}