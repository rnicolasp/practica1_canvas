package com.paint.servlets.controllers;

import com.paint.servlets.DAOS.CanvasDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para mostrar un canvas guardado en modo de solo lectura.
 */
@WebServlet("/viewCanvas")
public class ViewCanvasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;

        // Aunque la página es de "solo vista", requerimos sesión para saber
        // de quién cargar el canvas, ya que el DAO lo requiere.
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fileToLoad = req.getParameter("loadFile");
        if (fileToLoad == null || fileToLoad.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile"); // Redirigir si no hay archivo
            return;
        }

        // Usamos el DAO para cargar el contenido JSON del canvas.
        // Nota: CanvasDAO.loadPayload comprueba que el 'user' sea el propietario.
        String canvasData = CanvasDAO.loadPayload(user, fileToLoad);

        // Pasamos los datos del canvas (como string JSON) al JSP.
        // Si es null (no encontrado o no es propietario), el JSP mostrará un mensaje.
        req.setAttribute("canvasData", canvasData);
        req.getRequestDispatcher("WEB-INF/jsp/viewCanvas.jsp").forward(req, resp);
    }
}