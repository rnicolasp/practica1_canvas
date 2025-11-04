package com.paint.servlets.controllers;

import com.paint.servlets.services.UserService;
import com.paint.servlets.models.Canvas;

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
        UserService userService = new UserService();
        String name = userService.getName(user);

        req.setAttribute("name", name);
        req.setAttribute("user", user);

        // list saved canvas entries for this user via DAO
        try {
            java.util.List<Canvas> saves = com.paint.servlets.DAOS.CanvasDAO.list(user);
            req.setAttribute("saves", saves);
        } catch (Exception ex) {
            req.setAttribute("saves", java.util.Collections.emptyList());
        }

        req.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(req, resp);
    }
}
