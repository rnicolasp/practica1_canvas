package com.paint.servlets.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/canvas")
public class CanvasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ws = req.getParameter("width");
        String hs = req.getParameter("height");

        int width = 800;
        int height = 600;
        try {
            if (ws != null) width = Integer.parseInt(ws);
            if (hs != null) height = Integer.parseInt(hs);
        } catch (NumberFormatException e) {
            // keep defaults
        }

        req.setAttribute("width", width);
        req.setAttribute("height", height);

        req.getRequestDispatcher("WEB-INF/jsp/canvas.jsp").forward(req, resp);
    }
}
