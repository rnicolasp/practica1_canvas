package com.paint.servlets.controllers;

import com.paint.servlets.services.CanvasService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/saveCanvas")
public class SaveCanvasController extends HttpServlet {
    private final CanvasService canvasService = new CanvasService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String content = req.getReader().lines().collect(Collectors.joining());
        String name = req.getParameter("name");
        String id = req.getParameter("id");

        String objectCountStr = req.getParameter("objectCount");
        int objectCount = 0;
        try {
            objectCount = Integer.parseInt(objectCountStr);
        } catch (NumberFormatException e) {
        }

        if (name == null || name.trim().isEmpty()) {
            name = "sin_nombre";
        }

        String savedId = canvasService.saveCanvas(user, name, content, id, objectCount);

        if (savedId == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Save failed. Permission denied or canvas not found.");
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = "{\"status\": \"ok\", \"id\": \"" + savedId + "\"}";
        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}

