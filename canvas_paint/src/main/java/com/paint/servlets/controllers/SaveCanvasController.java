package com.paint.servlets.controllers;

import com.paint.servlets.DAOS.CanvasDAO;
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
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String user = (String) session.getAttribute("user");
        String content = req.getReader().lines().collect(Collectors.joining());
        String name = req.getParameter("name");
        
        if (name == null || name.trim().isEmpty()) {
            name = "Mi Canvas";
        }
        
        CanvasDAO.save(user, name, content, null);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String content = CanvasDAO.loadPayload(
            (String) session.getAttribute("user"),
            req.getParameter("file")
        );
        
        if (content != null) {
            resp.setContentType("application/json");
            resp.getWriter().write(content);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

