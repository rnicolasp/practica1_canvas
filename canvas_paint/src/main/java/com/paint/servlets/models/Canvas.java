package com.paint.servlets.models;

public class Canvas {
    private String id;
    private String owner; // username
    private String name; // display name
    private String content; // JSON payload
    private String filename; // generated filename or provided filename

    public Canvas(String id, String owner, String name, String content, String filename) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.content = content;
        this.filename = filename;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }
}

