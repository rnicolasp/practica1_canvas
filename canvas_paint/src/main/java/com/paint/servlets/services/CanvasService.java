package com.paint.servlets.services;

import com.paint.servlets.DAOS.CanvasDAO;
import com.paint.servlets.models.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void saveCanvas(Canvas canvas) {
        canvasDAO.registerCanvas(canvas);
    }

    public Canvas getCanvasById(int id) {
        return canvasDAO.findById(id);
    }

}