package com.paint.servlets.controllers;

import com.paint.servlets.DAOS.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }
        String name = UserDAO.getName(user);

        req.setAttribute("name", name);
        req.setAttribute("user", user);

        // list saved canvas files for this user (if any)
        try {
            String userHome = System.getProperty("user.home");
            java.nio.file.Path base = java.nio.file.Paths.get(userHome, "canvas_paint_saves", user);
            java.util.List<String> saves = new java.util.ArrayList<>();
            if (java.nio.file.Files.exists(base)) {
                java.io.File[] files = base.toFile().listFiles((d, name1) -> name1.endsWith(".json"));
                if (files != null) {
                    java.util.Arrays.sort(files, (a,b) -> Long.compare(b.lastModified(), a.lastModified()));
                    for (java.io.File f : files) saves.add(f.getName());
                }
            }
            req.setAttribute("saves", saves);
        } catch (Exception ex) {
            // ignore listing errors
            req.setAttribute("saves", java.util.Collections.emptyList());
        }

        req.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(req, resp);
    }
}
