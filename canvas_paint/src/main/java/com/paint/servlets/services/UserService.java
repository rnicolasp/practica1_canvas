package com.paint.servlets.services;

import com.paint.servlets.DAOS.UserDAO;

public class UserService {

    public UserService() {
    }

    public boolean checkUser(String username, String password) {
        return UserDAO.checkUser(username, password);
    }

    public boolean userExists(String username) {
        return UserDAO.userExists(username);
    }

    public void registerUser(String name, String user, String password) {
        UserDAO.registerUser(name, user, password);
    }

    public String getName(String username) {
        return UserDAO.getName(username);
    }
}
