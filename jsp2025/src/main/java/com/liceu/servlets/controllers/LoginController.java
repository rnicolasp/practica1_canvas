package com.liceu.servlets.controllers;

import com.liceu.servlets.DAOS.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginController  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/jsp/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        UserDAO userDAO = new UserDAO();
        boolean userExists = userDAO.CheckUser(user, password);
        if (userExists) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            resp.sendRedirect("/private");
            return;
        } else {
            req.setAttribute("message","Username or password invalid");
        }

        req.getRequestDispatcher("WEB-INF/jsp/login.jsp")
                .forward(req, resp);
    }
}
