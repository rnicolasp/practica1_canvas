package com.paint.servlets.DAOS;

import com.paint.servlets.models.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CanvasDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Canvas> rowMapper() {
        return (rs, rowNum) -> new Canvas(
                rs.getInt("id"),
                rs.getString("owner"),
                rs.getString("name"),
                rs.getString("content"),
                rs.getTimestamp("dateCreated"),
                rs.getTimestamp("dateModified"),
                rs.getBoolean("paperBin")
        );
    }

    public void registerCanvas(Canvas canvas) {
        String sql = "INSERTO INTO Canvas (owner,name,content,paperbin,) VALUES (?,?,?,?)";

        jdbcTemplate.update(sql,canvas.getOwner(),canvas.getName(),canvas.getContent(),canvas.getPaperBin(), new Date(), new Date());

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

}