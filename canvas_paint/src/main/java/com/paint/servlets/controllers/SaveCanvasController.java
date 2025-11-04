package com.paint.servlets.controllers;

import com.paint.servlets.DAOS.CanvasDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
 
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/saveCanvas")
public class SaveCanvasController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("/login");
            return;
        }
        String user = (String) session.getAttribute("user");

        // Read request body (JSON)
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        String body = sb.toString();

    // Use DAO to save a named canvas
    String displayName = req.getParameter("name");
    if (displayName == null) displayName = "canvas";
    String fileParam = req.getParameter("file");
    String filename = CanvasDAO.save(user, displayName, body, fileParam);

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"status\":\"ok\",\"file\":\"" + filename + "\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("/login");
            return;
        }
        String user = (String) session.getAttribute("user");
        String fileParam = req.getParameter("file");
        if (fileParam == null || fileParam.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("");
            return;
        }
        // first try to load from in-memory DAO (saved during current app run)
        String payload = com.paint.servlets.DAOS.CanvasDAO.loadPayload(user, fileParam);
        if (payload != null) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(payload);
            return;
        }

        // fallback: try reading from user's filesystem saves (existing behavior)
        String userHome = System.getProperty("user.home");
        Path file = Paths.get(userHome, "canvas_paint_saves", user, fileParam);
        if (!Files.exists(file)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("");
            return;
        }
        byte[] data = Files.readAllBytes(file);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getOutputStream().write(data);y
    }
}
