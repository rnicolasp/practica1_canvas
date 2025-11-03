package com.paint.servlets.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/newcanvas")
public class NewCanvasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }

        // Redirect to the canvas page; canvas.jsp contains an inline menu for creating/resizing canvases.
        resp.sendRedirect(req.getContextPath() + "/canvas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ws = req.getParameter("width");
        String hs = req.getParameter("height");

        int width = 500;
        int height = 500;
        try {
            if (ws != null) width = Math.max(100, Math.min(2000, Integer.parseInt(ws)));
            if (hs != null) height = Math.max(100, Math.min(2000, Integer.parseInt(hs)));
        } catch (NumberFormatException e) {
        }

        resp.sendRedirect(req.getContextPath() + "/canvas?width=" + width + "&height=" + height);
    }
}
