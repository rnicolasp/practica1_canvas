package com.paint.servlets.DAOS;

import com.paint.servlets.models.Canvas;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CanvasDAO {

    private static Canvas mapResultSetToCanvas(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String owner = rs.getString("owner");
        String name = rs.getString("name");
        String content = rs.getString("content");
        String filename = rs.getString("filename");
        Date dateCreated = rs.getTimestamp("dateCreated");
        Date dateModified = rs.getTimestamp("dateModified");
        int objectCount = rs.getInt("objectCount");

        return new Canvas(id, owner, name, content, filename, dateCreated, dateModified, objectCount);
    }

    //public static Canvas getCanvasById(String id) {

    //}

    public static String save(String user, String name, String content, String id, int objectCount) {
        return "";
    }
/*
    public static List<Canvas> list(String user) {
        List<Canvas> userCanvas = new ArrayList<>();
        String sql = "SELECT * FROM canvas WHERE owner = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userCanvas.add(mapResultSetToCanvas(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCanvas;
    }

    public static void delete(String user, String id) {
        String sql = "DELETE FROM canvas WHERE id = ? AND owner = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, user);
            ps.executeUpdate();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static String loadCanvasContentById(String id) {
        String sql = "SELECT content FROM canvas WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("content");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Canvas> listAll() {
        List<Canvas> allCanvas = new ArrayList<>();
        String sql = "SELECT * FROM canvas";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                allCanvas.add(mapResultSetToCanvas(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCanvas;
    }

 */
}