package com.paint.servlets.controllers;

import com.paint.servlets.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class userController {

    @Autowired
    UserService userService;

    @GetMapping("login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("login")
    public String processLogin(@RequestParam("user") String user, @RequestParam("password") String password, HttpSession session, Model model) {
        String userNoCaps = user.toLowerCase();

        if (userService.checkUser(userNoCaps, password)) {
            session.setAttribute("user", userNoCaps);
            return "redirect:/home"; // Redirección limpia
        } else {
            model.addAttribute("message", "Usuario o contraseña inválidos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
