package com.qikkle.barcodemapping.session;


import com.qikkle.barcodemapping.model.User;

public class SessionData {

    private static SessionData instance;
    private User user;

    private SessionData(){}

    public static SessionData getInstance() {
        if (instance == null) {
            instance = new SessionData();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
