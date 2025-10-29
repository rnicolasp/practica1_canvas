package com.liceu.servlets.controllers;

import com.liceu.servlets.models.Person;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/taula")
public class PrimersTaulaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Integer> list = calculaPrimers(500);
        List<List<Integer>> taula = new ArrayList<>();
        int pos = 0;
        while (pos < list.size()) {
            List<Integer> row = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                row.add(list.get(pos));
                pos++;
            }
            taula.add(row);
        }
        System.out.println(taula);
        req.setAttribute("taula", taula);

        Person p = new Person();
        p.setName("Elena");
        p.setBirthYear(2001);
        req.setAttribute("elena", p);

        Map<String, Integer> map = new HashMap<>();
        map.put("Dilluns", 4);
        map.put("Dimarts", 3);
        map.put("Dimecres", 3);
        map.put("Dijous", 5);
        req.setAttribute("dies", map);

        req.getRequestDispatcher("/WEB-INF/jsp/taula.jsp")
                .forward(req, resp);
    }

    List<Integer> calculaPrimers(int max) {
        int[] ar = new int[max];
        List<Integer> llistaPrimers = new ArrayList<>();
        for (int i = 0; i < ar.length; i++) {
            ar[i] = i+2;
        }
        for (int i = 0; i < ar.length; i++) {
            int primer = ar[i];
            if (primer == -1) continue;
            llistaPrimers.add(primer);
            int index = i;
            do {
                ar[index] = -1;
                index += primer;
            } while(index<ar.length);
        }
        return llistaPrimers;
    }
}
