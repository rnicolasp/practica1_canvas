package com.paint.servlets.services;

import com.paint.servlets.DAOS.CanvasDAO;
import com.paint.servlets.models.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CanvasService {

    @Autowired
    private CanvasDAO canvasDAO;

    public List<Canvas> listAllCanvas() {
        return canvasDAO.findAll();
    }

    public List<Canvas> listByUser(String user) {
        return canvasDAO.findByOwner(user);
    }

    public int updateCanvas(String owner, String name, String content, Integer id, boolean isPublic) {
        if (id != null && id > 0) {
            canvasDAO.updateCanvas(id, content, name, isPublic);
            return id;

        } else {
            Canvas canvas = new Canvas(0,owner,name,content,new Date(),new Date(),false, isPublic,1);
            return canvasDAO.registerCanvas(canvas);
        }
    }

    public Canvas getCanvasById(int id) { return canvasDAO.findById(id); }

    public void deletePermanent(String user, int id) { canvasDAO.deletePermanent(user,id); }

    public void moveToTrash(int id) { canvasDAO.moveToTrash(id, true); }

    public void restoreFromTrash(int id) { canvasDAO.moveToTrash(id, false); }

    public List<Canvas> listActiveByUser(String user) { return canvasDAO.getActiveCanvasByUser(user); }

    public List<Canvas> listTrashByUser(String user) { return canvasDAO.getTrashCanvasByUser(user); }

}