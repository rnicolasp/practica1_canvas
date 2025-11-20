package com.paint.servlets.models;

import java.util.Date;

public class Canvas {
    private String id;
    private String owner;
    private String name;
    private String content;
    private String filename;

    private Date dateCreated;
    private Date dateModified;
    private int objectCount;

    public Canvas(String id, String owner, String name, String content, String filename, Date dateCreated, Date dateModified, int objectCount) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.content = content;
        this.filename = filename;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.objectCount = objectCount;
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public String getName() { return name; }
    public String getContent() { return content; }
    public String getFilename() { return filename; }

    public Date getDateCreated() { return dateCreated; }
    public Date getDateModified() { return dateModified; }
    public int getObjectCount() { return objectCount; }
}