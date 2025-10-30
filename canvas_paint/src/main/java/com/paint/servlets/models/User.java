package com.paint.servlets.models;

public class User {
    private final String name;
    private final String user;
    private final String password;

    public User(String name, String user, String password) {
        this.name = name;
        this.user = user;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}