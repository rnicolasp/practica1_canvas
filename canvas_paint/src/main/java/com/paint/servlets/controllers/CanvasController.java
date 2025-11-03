package com.paint.servlets.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/canvas")
public class CanvasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }
        String ws = req.getParameter("width");
        String hs = req.getParameter("height");

        int width = 800;
        int height = 600;

        req.setAttribute("width", width);
        req.setAttribute("height", height);

        req.getRequestDispatcher("WEB-INF/jsp/canvas.jsp").forward(req, resp);
    }
}
