package com.liceu.servlets.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/game")
public class GameController  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session =  req.getSession();
        int secret = (int)(Math.random()*20);
        session.setAttribute("secret", secret);
        session.setAttribute("gameover", false);
        session.setAttribute("tries", 5);
        System.out.println(secret);
        req.getRequestDispatcher("WEB-INF/jsp/game.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int numero = Integer.parseInt(req.getParameter("numero"));
        HttpSession session = req.getSession();
        int secret = (int) session.getAttribute("secret");
        int tries = (int) session.getAttribute("tries");
        boolean gameover = (boolean) session.getAttribute("gameover");

        String msg = "";
        if (numero < secret) {
            msg = "El secret es major";
            tries--;
        } else if (numero == secret) {
            msg = "Has encertat!!";
            gameover = true;
        } else {
            tries--;
            msg = "El secret es menor";
        }
        if (tries==0) {
            gameover=true;
            msg = "Has perdut LOL";
        }
        req.setAttribute("msg", msg);
        req.setAttribute("gameover", gameover);
        req.setAttribute("tries", tries);

        session.setAttribute("tries", tries);
        session.setAttribute("gameover", gameover);


        req.getRequestDispatcher("WEB-INF/jsp/game.jsp")
                .forward(req, resp);
    }
}
