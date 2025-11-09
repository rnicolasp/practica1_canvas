package com.paint.servlets.services;

import com.paint.servlets.DAOS.CanvasDAO;
import com.paint.servlets.models.Canvas;
import java.util.List;

public class CanvasService {

    public CanvasService() {
    }

    public String saveCanvas(String user, String name, String content, String fileParam) {
        return CanvasDAO.save(user, name, content);
    }

    public List<Canvas> listUserCanvas(String user) {
        return CanvasDAO.list(user);
    }

    public void deleteCanvas(String user, String filename) {
        CanvasDAO.delete(user, filename);
    }

    public String loadCanvasContent(String user, String filename) {
        return CanvasDAO.loadCanvas(filename);
    }

    public List<Canvas> listAllCanvas() {
        return CanvasDAO.listAll();
    }
}