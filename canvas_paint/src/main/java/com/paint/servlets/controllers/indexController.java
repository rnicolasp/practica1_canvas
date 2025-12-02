package com.paint.servlets.controllers;

import com.paint.servlets.models.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.paint.servlets.services.CanvasService;
import com.paint.servlets.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class indexController {

    @Autowired
    private UserService userService;

    @Autowired
    private CanvasService canvasService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        List<Canvas> allCanvas = canvasService.listAllCanvas();
        model.addAttribute("allCanvas", allCanvas);

        return "home";
    }

    @GetMapping("/profile")
    private String profile(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        String name = userService.getName(user);
        if (user == null) {
            return "redirect:/login";
        }

        List<Canvas> canvasUser = canvasService.listByUser(user);
        model.addAttribute("canvasUser", canvasUser);

        model.addAttribute("user", user);
        model.addAttribute("name", name);

        return "profile";
    }
}
