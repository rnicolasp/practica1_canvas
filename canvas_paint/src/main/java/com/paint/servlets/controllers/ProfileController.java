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

        // Placeholder stats - in a real app these would come from a database/service
        req.setAttribute("name", name);
        req.setAttribute("user", user);
        //req.setAttribute("canvasesCreated", 3);
        //req.setAttribute("memberSince", "2023-01-01");

        req.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(req, resp);
    }
}
