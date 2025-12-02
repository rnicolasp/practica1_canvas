package com.paint.servlets.controllers;

import com.paint.servlets.services.CanvasService;
import com.paint.servlets.models.Canvas;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private final CanvasService canvasService = new CanvasService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }

        List<Canvas> allCanvas = canvasService.listAllCanvas();
        req.setAttribute("allCanvas", allCanvas);
        req.setAttribute("user", user);

        req.getRequestDispatcher("WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}