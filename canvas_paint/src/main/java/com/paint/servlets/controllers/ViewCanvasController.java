package com.paint.servlets.controllers;

import com.paint.servlets.DAOS.CanvasDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/viewCanvas")
public class ViewCanvasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fileToLoad = req.getParameter("loadFile");
        if (fileToLoad == null || fileToLoad.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        String canvasData = CanvasDAO.loadCanvas(fileToLoad);

        req.setAttribute("canvasData", canvasData);
        req.getRequestDispatcher("WEB-INF/jsp/viewCanvas.jsp").forward(req, resp);
    }
}