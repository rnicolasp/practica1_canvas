package com.liceu.servlets.DAOS;

public class UserDAO {
    public boolean CheckUser(String user, String password) {
        if (user.equals("elena") && password.equals("1234")) return true;
        if (user.equals("jules") && password.equals("2001")) return true;
        return false;
    }
}
