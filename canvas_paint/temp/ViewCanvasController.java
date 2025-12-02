package com.paint.servlets.controllers;

import com.paint.servlets.services.CanvasService; // Importar servicio

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/viewCanvas")
public class ViewCanvasController extends HttpServlet {
    private final CanvasService canvasService = new CanvasService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String loadId = req.getParameter("loadId");
        if (loadId == null || loadId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        String canvasData = canvasService.loadCanvasContentById(loadId);

        if (canvasData == null) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        req.setAttribute("canvasData", canvasData);
        req.getRequestDispatcher("WEB-INF/jsp/viewCanvas.jsp").forward(req, resp);
    }
}