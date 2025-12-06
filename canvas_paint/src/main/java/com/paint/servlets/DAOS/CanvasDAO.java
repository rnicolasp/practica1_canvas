package com.paint.servlets.DAOS;

import com.paint.servlets.models.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
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
        String sql = "UPDATE canvas SET content = ?, name = ?, is_public = ? WHERE id = ?";
        jdbcTemplate.update(sql, content, name, isPublic, id);
    }

    public List<Canvas> findAll() {
        String sql = "SELECT * FROM canvas WHERE paperBin = false";
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



}