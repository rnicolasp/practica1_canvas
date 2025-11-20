package com.paint.servlets.controllers;

import com.paint.servlets.services.CanvasService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deleteCanvas")
public class DeleteCanvasController extends HttpServlet {
    private final CanvasService canvasService = new CanvasService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session == null) {
            resp.sendRedirect("/login");
            return;
        }
        
        String user = (String) session.getAttribute("user");
        String id = req.getParameter("id");

        if (id != null && !id.isEmpty()) {
            canvasService.deleteCanvas(user, id);
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}