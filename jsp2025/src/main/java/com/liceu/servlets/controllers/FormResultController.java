package com.liceu.servlets.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/result")
public class FormResultController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s1 = req.getParameter("var1");
        String s2 = req.getParameter("var2");
        String op = req.getParameter("operacio");
        int i1 = Integer.parseInt(s1);
        int i2 = Integer.parseInt(s2);
        int result = 0;
        switch (op) {
            case "suma":
                result = i1 + i2;
                break;
            case "resta":
                result = i1 - i2;
                break;
            case "multiplicacio":
                result = i1 * i2;
                break;
            case "divisio":
                result = i1 / i2;
                break;
        }

        req.setAttribute("result", result);
        req.setAttribute("op", op);

        req.getRequestDispatcher("WEB-INF/jsp/result.jsp")
                .forward(req,resp);
    }
}
