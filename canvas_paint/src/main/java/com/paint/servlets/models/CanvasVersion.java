package com.paint.servlets.models;

import java.util.Date;

public class CanvasVersion {
    private int id;
    private int canvasId;
    private String content;
    private int versionNumber;
    private Date savedAt;

    public CanvasVersion(int id, int canvasId, String content, int versionNumber, Date savedAt) {
        this.id = id;
        this.canvasId = canvasId;
        this.content = content;
        this.versionNumber = versionNumber;
        this.savedAt = savedAt;
    }

    // Getters
    public int getId() { return id; }
    public int getCanvasId() { return canvasId; }
    public String getContent() { return content; }
    public int getVersionNumber() { return versionNumber; }
    public Date getSavedAt() { return savedAt; }
}