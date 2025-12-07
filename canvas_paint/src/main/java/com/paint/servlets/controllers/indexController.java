package com.paint.servlets.controllers;

import com.paint.servlets.models.Canvas;
import com.paint.servlets.models.CanvasVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.paint.servlets.services.CanvasService;
import com.paint.servlets.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class indexController {

    public record RespuestaGuardado(String status, Object id) { }

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
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        List<Canvas> allCanvas = canvasService.listAllCanvas();
        model.addAttribute("allCanvas", allCanvas);
        List<Canvas> sharedCanvas = canvasService.listSharedWithUser(user);
        model.addAttribute("sharedCanvas", sharedCanvas);
        List<Integer> editableIds = canvasService.getWriteIds(user);
        model.addAttribute("editableIds", editableIds);
        return "home";
    }

    @GetMapping("/profile")
    private String profile(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        String name = userService.getName(user);
        List<Canvas> canvasUser = canvasService.listByUser(user);

        model.addAttribute("canvasUser", canvasUser);
        model.addAttribute("user", user);
        model.addAttribute("name", name);
        return "profile";
    }

    @GetMapping("/canvas")
    private String showCanvas(@RequestParam(value = "id", required = false) Integer id, HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Canvas canvas = canvasService.getCanvasById(id);
        if (canvas != null) {
            boolean isOwner = user.equals(canvas.getOwner());
            boolean canEdit = canvasService.canEdit(id, user);
            model.addAttribute("loadId", canvas.getId());
            model.addAttribute("ownerName", canvas.getOwner());
            model.addAttribute("canvasName", canvas.getName());
            model.addAttribute("canvasData", canvas.getContent());
            model.addAttribute("isOwner", isOwner);
            model.addAttribute("canEdit", canEdit);
        }
        return "canvas";
    }

    @GetMapping("/editor")
    public String editor(@RequestParam(value = "id", required = false) Integer id, HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("width", 600);
        model.addAttribute("height", 600);
        boolean isOwner = true;
        boolean canEdit = true;

        if (id != null) {
            Canvas canvas = canvasService.getCanvasById(id);
            if (canvas != null && canvasService.canEdit(id, user)) {
                isOwner = canvas.getOwner().equals(user);
                model.addAttribute("loadId", canvas.getId());
                model.addAttribute("canvasName", canvas.getName());
                model.addAttribute("canvasData", canvas.getContent());
                model.addAttribute("privacyValue", canvas.isPublic() ? "1" : "0");
            } else {
                return "redirect:/profile";
            }
        }

        model.addAttribute("isOwner", isOwner);
        model.addAttribute("canEdit", canEdit);
        return "editor";
    }

    @PostMapping("/saveCanvas")
    @ResponseBody
    public RespuestaGuardado saveCanvas(@RequestParam("name") String name,
                                        @RequestParam(value = "id", required = false) Integer id,
                                        @RequestParam(value = "isPublic", required = false, defaultValue = "false") boolean isPublic,
                                        @RequestBody String content,
                                        HttpSession session) {

        String user = (String) session.getAttribute("user");
        if (user == null) {
            return new RespuestaGuardado("error", "No has iniciado sesión");
        }
        try {
            if (id != null) {
                if (!canvasService.canEdit(id, user)) {
                    return new RespuestaGuardado("error", "No tienes permiso de edición.");
                }
            }

            int savedId = canvasService.updateCanvas(user, name, content, id, isPublic);
            return new RespuestaGuardado("ok", savedId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaGuardado("error", "Error interno");
        }
    }

    @PostMapping("/deleteCanvas")
    public String deleteCanvas(@RequestParam("id") Integer id, HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "error_login";

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
        if (user == null) return "redirect:/login";

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
            canvasService.deletePermanent(user, id);
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/canvas/history")
    @ResponseBody
    public List<CanvasVersion> getCanvasHistory(@RequestParam("id") int id, HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) return null;

        Canvas canvas = canvasService.getCanvasById(id);
        if (canvas != null && (canvas.isPublic() || canvasService.canEdit(id, user) || canvas.getOwner().equals(user))) {
            return canvasService.getHistory(id);
        }
        return null;
    }

    @PostMapping("/share/add")
    @ResponseBody
    public String share(@RequestParam int id, @RequestParam String targetUser, @RequestParam String permission, HttpSession session) {
        String owner = (String) session.getAttribute("user");
        if (owner == null) return "error:login";
        try {
            canvasService.shareCanvas(owner, id, targetUser, permission);
            return "ok";
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    @PostMapping("/share/remove")
    @ResponseBody
    public String unshare(@RequestParam int id, @RequestParam String targetUser, HttpSession session) {
        String owner = (String) session.getAttribute("user");
        try {
            canvasService.unshareCanvas(owner, id, targetUser);
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/share/list")
    @ResponseBody
    public List<Map<String, Object>> listShares(@RequestParam int id) {
        return canvasService.getShares(id);
    }

    @PostMapping("/canvas/duplicate")
    public String duplicateCanvas(@RequestParam("id") Integer id, HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Canvas canvas = canvasService.getCanvasById(id);
        if (canvas != null) {
            boolean esPublico = canvas.isPublic();
            boolean esMio = canvas.getOwner().equals(user);
            boolean compartidoConmigo = canvasService.canEdit(id, user)
                    || canvasService.getShares(id).stream().anyMatch(s -> s.get("user_id").equals(user));
            if (esPublico || esMio || compartidoConmigo) {
                canvasService.duplicateCanvas(id, user);
                return "redirect:/home";
            }
        }

        return "redirect:/home?error=no_permiso";
    }
}