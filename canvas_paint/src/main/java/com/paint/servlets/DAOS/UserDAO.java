package com.paint.servlets.DAOS;

import com.paint.servlets.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final static List<User> userDatabase = new ArrayList<>();

    static {
        User localAdmin = new User("Elena","admin","admin");
        userDatabase.add(localAdmin);
    }

    public static boolean checkUser(String username, String password) {
        for (User user : userDatabase) {
            if (user.getUser().equals(username)) {
                return user.getPassword().equals(password);
            }
        }
        return false;
    }

    public static boolean userExists(String username){
        for (User user : userDatabase){
            if (user.getUser().equals(username)){
                return  true;
            }
        }
        return false;
    }

    public static String getName(String username){
        String result = "";
        for (User user : userDatabase){
            if (user.getUser().equals(username)) {
                result = user.getName();
                break;
            }
        }
        return result;
    }

    public static void registerUser(String name, String user, String password) {
        User nouRegistre = new User(name,user,password);
        userDatabase.add(nouRegistre);
    }
}
