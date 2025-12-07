package com.paint.servlets.DAOS;

import com.paint.servlets.models.Canvas;
import com.paint.servlets.models.CanvasVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CanvasDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public CanvasDAO(DataSource dataSource) {
        this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName("canvas").usingGeneratedKeyColumns("id");
    }

    private RowMapper<Canvas> rowMapper() {
        return (rs, rowNum) -> new Canvas(
                rs.getInt("id"),
                rs.getString("owner"),
                rs.getString("name"),
                rs.getString("content"),
                rs.getTimestamp("dateCreated"),
                rs.getTimestamp("dateModified"),
                rs.getBoolean("paperBin"),
                rs.getBoolean("isPublic"),
                rs.getInt("version")
        );
    }

    public int registerCanvas(Canvas canvas) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("owner", canvas.getOwner());
        parametros.put("name", canvas.getName());
        parametros.put("content", canvas.getContent());
        parametros.put("paperBin", false);
        parametros.put("dateCreated", new Date());
        parametros.put("dateModified", new Date());
        parametros.put("isPublic", canvas.isPublic());
        parametros.put("version", 1);

        Number nuevoId = simpleInsert.executeAndReturnKey(parametros);
        return nuevoId.intValue();
    }

    public void updateCanvas(int id, String content, String name, boolean isPublic) {
        String sql = "UPDATE canvas SET content = ?, name = ?, isPublic = ?, version = version + 1, dateModified = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, content, name, isPublic, id);
    }

    public List<Canvas> findAll() {
        String sql = "SELECT * FROM canvas WHERE paperBin = false AND isPublic = true";
        return jdbcTemplate.query(sql, rowMapper());
    }

    public List<Canvas> findByOwner(String owner) {
        String sql = "SELECT * FROM canvas WHERE paperBin = false AND owner = ?";
        return jdbcTemplate.query(sql, rowMapper(), owner);
    }

    public Canvas findById(int id) {
        String sql = "SELECT * FROM canvas WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public void moveToTrash(int id, boolean inTrash) {
        String sql = "UPDATE canvas SET paperBin = ? WHERE id = ?";
        jdbcTemplate.update(sql, inTrash, id);
    }

    public List<Canvas> getActiveCanvasByUser(String user) {
        String sql = "SELECT * FROM canvas WHERE owner = ? AND paperBin = false";
        return jdbcTemplate.query(sql, rowMapper(), user);
    }

    public List<Canvas> getTrashCanvasByUser(String user) {
        String sql = "SELECT * FROM canvas WHERE owner = ? AND paperBin = true";
        return jdbcTemplate.query(sql,rowMapper(), user);
    }

    public void deletePermanent(String user, int id) {
        String sql = "DELETE FROM canvas WHERE owner = ? AND id = ?";
        jdbcTemplate.query(sql,rowMapper(), user, id);
    }

    public void saveVersionHistory(int canvasId, String oldContent, int oldVersion) {
        String sql = "INSERT INTO canvas_versions (canvas_id, content, version_number, saved_at) VALUES (?, ?, ?, NOW())";
        jdbcTemplate.update(sql, canvasId, oldContent, oldVersion);
    }

    public List<CanvasVersion> getVersions(int canvasId) {
        String sql = "SELECT * FROM canvas_versions WHERE canvas_id = ? ORDER BY version_number DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CanvasVersion(
                rs.getInt("id"),
                rs.getInt("canvas_id"),
                rs.getString("content"),
                rs.getInt("version_number"),
                rs.getTimestamp("saved_at")
        ), canvasId);
    }

    public void shareCanvas(int canvasId, String targetUser, String permission) {
        String sql = "INSERT INTO shared_canvas (canvas_id, user_id, permission) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE permission = ?";
        jdbcTemplate.update(sql, canvasId, targetUser, permission, permission);
    }

    public void unshareCanvas(int canvasId, String targetUser) {
        String sql = "DELETE FROM shared_canvas WHERE canvas_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, canvasId, targetUser);
    }

    public List<Map<String, Object>> getSharedUsers(int canvasId) {
        String sql = "SELECT user_id, permission FROM shared_canvas WHERE canvas_id = ?";
        return jdbcTemplate.queryForList(sql, canvasId);
    }

    public String getUserPermission(int canvasId, String user) {
        String sql = "SELECT permission FROM shared_canvas WHERE canvas_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, canvasId, user);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}