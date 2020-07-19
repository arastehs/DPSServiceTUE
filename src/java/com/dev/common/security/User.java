package com.dev.common.security;

public abstract class User {
    protected HashingStrategy passStrategy;
    protected AccessLevel levelStrategy;
    protected String userName;
    protected String password;
    protected String level;

    public User(String userName, String password, String level) {
        this.userName = userName;
        this.password = password;
        this.level = level;
    }

    public abstract int getUserInfo(String userName, String pass) throws Exception;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
