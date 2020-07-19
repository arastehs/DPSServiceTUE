package com.dev.common.security;

import com.dev.model.dao.UserDao;

public class AdminUser extends User {
    public AdminUser(String userName, String password, String level) {
        super(userName, password, level);
        passStrategy = new ShaStrategy();
        levelStrategy = new AdminAccessLevel();
    }

    @Override
    public int getUserInfo(String userName, String pass) throws Exception {
        UserDao userDao = new UserDao();
        return userDao.getUserFromDB(userName,getSecurePass(),getAccess());
    }

    public String getAccess(){
        return levelStrategy.getAccessLevel();
    }
    public String getSecurePass() throws Exception {
        return passStrategy.createMac(password);
    }

}
