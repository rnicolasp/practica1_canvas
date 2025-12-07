package com.paint.servlets.services;

import com.paint.servlets.DAOS.CanvasDAO;
import com.paint.servlets.models.Canvas;
import com.paint.servlets.models.CanvasVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CanvasService {

    @Autowired
    private CanvasDAO canvasDAO;

    @Autowired
    private UserService userService;

    public List<Canvas> listAllCanvas() {
        return canvasDAO.findAll();
    }

    public List<Canvas> listByUser(String user) {
        return canvasDAO.findByOwner(user);
    }

    public int updateCanvas(String owner, String name, String content, Integer id, boolean isPublic) {
        if (id != null && id > 0) {
            Canvas currentCanvas = canvasDAO.findById(id);

            if (currentCanvas != null && canEdit(id, owner)) {
                canvasDAO.saveVersionHistory(
                        currentCanvas.getId(),
                        currentCanvas.getContent(),
                        currentCanvas.getVersion()
                );
                canvasDAO.updateCanvas(id, content, name, isPublic);
                return id;
            } else {
                Canvas canvas = new Canvas(0, owner, name, content, new Date(), new Date(), false, isPublic, 1);
                return canvasDAO.registerCanvas(canvas);
            }
        } else {
            Canvas canvas = new Canvas(0, owner, name, content, new Date(), new Date(), false, isPublic, 1);
            return canvasDAO.registerCanvas(canvas);
        }
    }

    public Canvas getCanvasById(int id) { return canvasDAO.findById(id); }

    public void deletePermanent(String user, int id) { canvasDAO.deletePermanent(user,id); }

    public void moveToTrash(int id) { canvasDAO.moveToTrash(id, true); }

    public void restoreFromTrash(int id) { canvasDAO.moveToTrash(id, false); }

    public List<Canvas> listActiveByUser(String user) { return canvasDAO.getActiveCanvasByUser(user); }

    public List<Canvas> listTrashByUser(String user) { return canvasDAO.getTrashCanvasByUser(user); }

    public List<CanvasVersion> getHistory(int canvasId) {
        return canvasDAO.getVersions(canvasId);
    }

    public void shareCanvas(String owner, int canvasId, String targetUser, String permission) {
        Canvas c = canvasDAO.findById(canvasId);

        if (c == null || !c.getOwner().equals(owner)) {
            throw new RuntimeException("Solo el propietario puede compartir.");
        }

        if (!userService.userExists(targetUser)) {
            throw new RuntimeException("El usuario destino no existe.");
        }

        if (owner.equals(targetUser)) {
            throw new RuntimeException("No puedes compartir a ti mismo.");
        }

        canvasDAO.shareCanvas(canvasId, targetUser, permission);
    }

    public void unshareCanvas(String owner, int canvasId, String targetUser) {
        Canvas c = canvasDAO.findById(canvasId);
        if (c != null && c.getOwner().equals(owner)) {
            canvasDAO.unshareCanvas(canvasId, targetUser);
        }
    }

    public List<Map<String, Object>> getShares(int canvasId) {
        return canvasDAO.getSharedUsers(canvasId);
    }

    public boolean canEdit(int canvasId, String user) {
        Canvas c = canvasDAO.findById(canvasId);
        if (c == null) return false;
        if (c.getOwner().equals(user)) return true;
        String perm = canvasDAO.getUserPermission(canvasId, user);
        return "WRITE".equals(perm);
    }

}