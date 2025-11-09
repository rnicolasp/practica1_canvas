package com.paint.servlets.controllers;

import com.paint.servlets.services.UserService;
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


@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    private final UserService userService = new UserService();
    private final CanvasService canvasService = new CanvasService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }
        String name = userService.getName(user);

        req.setAttribute("name", name);
        req.setAttribute("user", user);

        List<Canvas> saves = canvasService.listUserCanvas(user);
        req.setAttribute("saves", saves);

        req.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(req, resp);
    }
}
