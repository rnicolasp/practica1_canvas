package com.paint.servlets.controllers;

import com.paint.servlets.models.Canvas;
import com.paint.servlets.services.CanvasService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CanvasController {

    @Autowired
    private CanvasService canvasService;
    /*

    @GetMapping("/canvas")
    public String showCanvas(@RequestParam(value = "loadId", required = false) String loadId,
                             HttpSession session,
                             Model model) {

        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (loadId != null && !loadId.trim().isEmpty()) {
            Canvas canvas = canvasService.getCanvasById(loadId);

            if (canvas != null) {
                boolean isOwner = user.equals(canvas.getOwner());

                if (isOwner) {
                    model.addAttribute("loadId", canvas.getId());
                    model.addAttribute("canvasName", canvas.getName());
                    model.addAttribute("canvasData", canvas.getContent());
                    model.addAttribute("isOwner", true);
                } else {
                    return "redirect:/viewCanvas?loadId=" + canvas.getId();
                }
            } else {
                return "redirect:/profile";
            }
        } else {
            model.addAttribute("isOwner", true);
        }

        model.addAttribute("width", 800);
        model.addAttribute("height", 600);

        return "canvas";
    }

     */
}