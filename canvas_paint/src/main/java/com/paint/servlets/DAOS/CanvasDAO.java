package com.paint.servlets.DAOS;

import com.paint.servlets.models.Canvas;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CanvasDAO {
    private static final List<Canvas> canvasDatabase = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public static synchronized String save(String user, String name, String content, String fileParam) {
        String id = String.valueOf(idCounter.getAndIncrement());
        String filename = name.replaceAll("\\s+", "_") + "_" + id + ".json";
        
        Canvas canvas = new Canvas(id, user, name, content, filename);
        canvasDatabase.add(canvas);
        return filename;
    }

    public static List<Canvas> list(String user) {
        List<Canvas> userCanvas = new ArrayList<>();
        for (Canvas canvas : canvasDatabase) {
            if (user.equals(canvas.getOwner())) {
                userCanvas.add(canvas);
            }
        }
        return userCanvas;
    }

    public static boolean delete(String user, String filename) {
        return canvasDatabase.removeIf(canvas -> 
            user.equals(canvas.getOwner()) && filename.equals(canvas.getFilename())
        );
    }

    public static String loadPayload(String user, String filename) {
        for (Canvas canvas : canvasDatabase) {
            if (user.equals(canvas.getOwner()) && filename.equals(canvas.getFilename())) {
                return canvas.getContent();
            }
        }
        return null;
    }

    public static List<Canvas> listAll() {
        return new ArrayList<>(canvasDatabase);
    }
}

