package com.paint.servlets.controllers;

import com.paint.servlets.models.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.paint.servlets.services.CanvasService;
import com.paint.servlets.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class indexController {

    public record RespuestaGuardado(String status, Object id) {
    }

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

    @GetMapping("/canvas")
    private String showCanvas(@RequestParam(value = "id", required = false) Integer id, HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Canvas canvas = canvasService.getCanvasById(id);
        if (canvas != null) {
            boolean isOwner = user.equals(canvas.getOwner());

            model.addAttribute("loadId", canvas.getId());
            model.addAttribute("ownerName", canvas.getOwner());
            model.addAttribute("canvasName", canvas.getName());
            model.addAttribute("canvasData", canvas.getContent());
            model.addAttribute("isOwner", isOwner);
        }


        return "canvas";
    }

    @GetMapping("/editor")
    private String editor(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("width", 600);
        model.addAttribute("height", 600);
        model.addAttribute("isOwner", true);

        return "/editor";
    }


    @PostMapping("/saveCanvas")
    @ResponseBody
    public RespuestaGuardado saveCanvas(@RequestParam("name") String name,@RequestParam(value = "id", required = false) Integer id,
                                        @RequestParam(value = "isPublic", required = false, defaultValue = "false") boolean isPublic,
                                        @RequestBody String content,
                                        HttpSession session) {

        String user = (String) session.getAttribute("user");
        if (user == null) {
            return new RespuestaGuardado("error", "No has iniciado sesión");
        }
        try {
            int savedId = canvasService.updateCanvas(user, name, content, id, isPublic);
            return new RespuestaGuardado("ok", savedId);
        } catch (Exception e) {
            return new RespuestaGuardado("error", "Error interno");
        }
    }

    @PostMapping("/deleteCanvas")
    public String deleteCanvas(@RequestParam("id") Integer id, HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "error_login";
        }
        Canvas canvas = canvasService.getCanvasById(id);
        if (canvas != null && canvas.getOwner().equals(user)) {
            canvasService.moveToTrash(id);
            return "redirect:/profile";
        } else {
            return "error_permission";
        }
    }

    @GetMapping("/bin")
    public String showBin(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Canvas> trashList = canvasService.listTrashByUser(user);
        model.addAttribute("trashList", trashList);
        model.addAttribute("user", user);
        return "bin";
    }

    @PostMapping("/bin/restore")
    @ResponseBody
    public String restoreCanvas(@RequestParam("id") int id) {
        try {
            canvasService.restoreFromTrash(id);
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/bin/deletePermanent")
    @ResponseBody
    public String deletePermanent(@RequestParam("id") int id, HttpSession session) {
        String user = (String) session.getAttribute("user");
        try {
            canvasService.deletePermanent(user,id);
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }
}
