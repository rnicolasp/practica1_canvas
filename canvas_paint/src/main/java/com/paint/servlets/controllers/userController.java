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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("user") String user, @RequestParam("password") String password, HttpSession session, Model model) {
        String userNoCaps = user.toLowerCase();

        if (userService.checkUser(userNoCaps, password)) {
            session.setAttribute("user", userNoCaps);
            return "redirect:/home";
        } else {
            model.addAttribute("message", "Usuario o contraseña inválidos");
            return "login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("name") String name, @RequestParam("user") String username, @RequestParam("password") String password, Model model) {
        String userNoCaps = username.toLowerCase();

        if (password == null || password.trim().length() < 5) {
            model.addAttribute("message", "La contrasenya ha de tenir almenys 5 caràcters");
            return "register";
        }

        if (userService.userExists(userNoCaps)) {
            model.addAttribute("message", "Usuari ja existeix");
            return "register";
        }

        userService.registerUser(name, userNoCaps, password);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
