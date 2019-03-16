package main.java.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.java.server.dao.UserDao;
import main.java.server.entity.User;



import java.util.ArrayList;
import java.util.List;


/*
This class will hold all the service-logic, it operates by initializing a UserDao object and calling it's methods.
This class gets called by the controller, to split HTTP requests from what methods they should be doing. Here just the logic applies.

 */
@Service
public class UserService {


    @Autowired
    private UserDao userDao;

    public void logIn(User user) {
        this.userDao.DBLogin(user);
    }

    public void registerUser(User user) {
        this.userDao.DBRegister(user);
    }

}