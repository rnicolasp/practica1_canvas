package com.paint.servlets.controllers;

import com.paint.servlets.services.UserService;
import com.paint.servlets.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        req.getRequestDispatcher("WEB-INF/jsp/register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        UserService userService = new UserService();

        if (password == null || password.trim().length() < 5) {
            req.setAttribute("message", "La contrasenya ha de tenir almenys 5 caràcters");
            req.getRequestDispatcher("WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        boolean userExists = userService.userExists(user);
        if (userExists) {
            req.setAttribute("message", "Usuari ja existeix");
            req.getRequestDispatcher("WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        } else {
            userService.registerUser(name, user, password);
        }

        req.getRequestDispatcher("WEB-INF/jsp/login.jsp")
                .forward(req, resp);
    }
}
