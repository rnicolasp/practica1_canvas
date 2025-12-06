package com.paint.servlets.models;

import java.util.Date;

public class Canvas {
    private int id;
    private String owner;
    private String name;
    private String content;
    private Date dateCreated;
    private Date dateModified;
    private boolean paperBin;
    private boolean isPublic;
    private int version;

    public Canvas(int id, String owner, String name, String content, Date dateCreated, Date dateModified, boolean paperBin, boolean isPublic, int version) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.content = content;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.paperBin = paperBin;
        this.isPublic = isPublic;
        this.version = version;
    }

    public int getId() { return id; }
    public String getOwner() { return owner; }
    public String getName() { return name; }
    public String getContent() { return content; }
    public Date getDateCreated() { return dateCreated; }
    public Date getDateModified() { return dateModified; }
    public boolean getPaperBin() { return paperBin; }
    public boolean isPublic() { return isPublic; }
    public int getVersion() { return version; }
}