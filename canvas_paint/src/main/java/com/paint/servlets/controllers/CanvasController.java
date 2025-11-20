package com.paint.servlets.controllers;

import com.paint.servlets.models.Canvas;
import com.paint.servlets.services.CanvasService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/canvas")
public class CanvasController extends HttpServlet {

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

        if (loadId != null && !loadId.trim().isEmpty()) {
            Canvas canvas = canvasService.getCanvasById(loadId);

            if (canvas != null) {
                boolean isOwner = user.equals(canvas.getOwner());

                if (isOwner) {
                    req.setAttribute("loadId", canvas.getId());
                    req.setAttribute("canvasName", canvas.getName());
                    req.setAttribute("canvasData", canvas.getContent());
                    req.setAttribute("isOwner", true);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/viewCanvas?loadId=" + canvas.getId());
                    return;
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            }
        } else {
            req.setAttribute("isOwner", true);
        }

        req.setAttribute("width", 800);
        req.setAttribute("height", 600);
        req.getRequestDispatcher("WEB-INF/jsp/canvas.jsp").forward(req, resp);
    }
}