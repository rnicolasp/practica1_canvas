package com.paint.servlets.DAOS;

import com.paint.servlets.models.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CanvasDAO {

    private final static List<Canvas> canvasDatabase = new ArrayList<>();

    // simple id generator
    private final static AtomicLong ID_COUNTER = new AtomicLong(1);

    // Save a canvas in the in-memory database. Returns the filename assigned to the saved canvas.
    public static synchronized String save(String user, String displayName, String content, String fileParam) {
        // determine id
        String id = idCanvas();

        // determine filename: if caller provided one (e.g. overwrite), use it; otherwise build one
        String filename;
        if (fileParam != null && !fileParam.trim().isEmpty()) {
            filename = fileParam;
        } else {
            // sanitize displayName basic: replace spaces with underscore and remove non-alnum except - _ .
            String sanitized = displayName == null ? "canvas" : displayName.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_\\-.]", "");
            filename = sanitized + "-" + id + ".json";
        }

        Canvas c = new Canvas(id, user, displayName, content, filename);
        canvasDatabase.add(c);
        return filename;
    }

    // Generate a new unique id (string)
    public static String idCanvas() {
        long v = ID_COUNTER.getAndIncrement();
        return Long.toString(v);
    }

    // List saved canvases for a given user
    public static List<Canvas> list(String user) {
        List<Canvas> out = new ArrayList<>();
        for (Canvas c : canvasDatabase) {
            if (c.getOwner() != null && c.getOwner().equals(user)) {
                out.add(c);
            }
        }
        return out;
    }

    // Delete a canvas by filename for a given user
    public static boolean delete(String user, String filename) {
        for (int i = 0; i < canvasDatabase.size(); i++) {
            Canvas c = canvasDatabase.get(i);
            if (c.getOwner() != null && c.getOwner().equals(user) && c.getFilename() != null && c.getFilename().equals(filename)) {
                canvasDatabase.remove(i);
                return true;
            }
        }
        return false;
    }

    // Load the raw payload (content) for a user's saved canvas by filename
    public static String loadPayload(String user, String filename) {
        for (Canvas c : canvasDatabase) {
            if (c.getOwner() != null && c.getOwner().equals(user) && c.getFilename() != null && c.getFilename().equals(filename)) {
                return c.getContent();
            }
        }
        return null;
    }

}

