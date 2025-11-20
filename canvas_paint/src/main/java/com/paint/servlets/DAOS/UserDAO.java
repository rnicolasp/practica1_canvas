package com.paint.servlets.DAOS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean checkUser(String username, String password) {
        String userNoCaps = username.toLowerCase();
        String sql = "SELECT 1 FROM users WHERE user = ? AND password = ?";

        System.out.println("Executing SQL: " + sql);
        System.out.println("  -> params: [user=" + userNoCaps + ", password=" + password + "]");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userNoCaps);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean userExists(String username) {
        String userNoCaps = username.toLowerCase();
        String sql = "SELECT 1 FROM users WHERE user = ?";

        System.out.println("Executing SQL: " + sql);
        System.out.println("  -> params: [user=" + userNoCaps + "]");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userNoCaps);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getName(String username) {
        String sql = "SELECT name FROM users WHERE user = ?";

        System.out.println("Executing SQL: " + sql);
        System.out.println("  -> params: [user=" + username + "]");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void registerUser(String name, String user, String password) {
        String userNoCaps = user.toLowerCase();
        String sql = "INSERT INTO users (name, user, password) VALUES (?, ?, ?)";

        System.out.println("Executing SQL: " + sql);
        System.out.println("  -> params: [name=" + name + ", user=" + userNoCaps + ", password=" + password + "]");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, userNoCaps);
            ps.setString(3, password);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}