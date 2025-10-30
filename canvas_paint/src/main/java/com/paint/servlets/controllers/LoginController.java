package com.paint.servlets.controllers;

import com.paint.servlets.DAOS.UserDAO;

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
        HttpSession session = req.getSession();
        session.invalidate();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        UserDAO userDAO = new UserDAO();
        boolean userExists = userDAO.checkUser(user, password);
        if (userExists) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            resp.sendRedirect("/home");
            return;
        } else {
            req.setAttribute("message","Username or password invalid");
        }

        req.getRequestDispatcher("WEB-INF/jsp/login.jsp")
                .forward(req, resp);
    }
}
