package com.paint.servlets.controllers;

import ch.qos.logback.core.model.Model;
import com.paint.servlets.services.CanvasService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {
    private CanvasService canvasService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        return "home";
    }
}
