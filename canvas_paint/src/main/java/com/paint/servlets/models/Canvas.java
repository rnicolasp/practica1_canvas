package com.paint.servlets.models;

public class Canvas {
    private String id;
    private String owner;
    private String name;
    private String content;
    private String filename;

    public Canvas(String id, String owner, String name, String content, String filename) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.content = content;
        this.filename = filename;
    }

    // Getters
    public String getId() { return id; }
    public String getOwner() { return owner; }
    public String getName() { return name; }
    public String getContent() { return content; }
    public String getFilename() { return filename; }
}

