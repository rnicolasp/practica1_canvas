package com.paint.servlets.services;

import com.paint.servlets.DAOS.UserDAO;
import com.paint.servlets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO UserDAO;

    public UserService() {
    }

    public boolean checkUser(String username, String password) {
        return UserDAO.checkUser(username, password);
    }

    public boolean userExists(String username) {
        return UserDAO.userExists(username);
    }

    public void registerUser(String name, String username, String password) {
        User user = new User(name,username,password);
        UserDAO.registerUser(user);
    }

    public String getName(String username) {
        return UserDAO.getName(username);
    }
}
