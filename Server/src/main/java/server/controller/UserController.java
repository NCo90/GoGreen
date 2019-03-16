package main.java.server.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import main.java.server.entity.User;
import main.java.server.service.UserService;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@RestController
@RequestMapping("/")    //This allows springboot to map all the HTTP requests to the server to this controller. MIGHT CONTAIN BUG
public class UserController {

    @Autowired
    private UserService userService;

    private List<User> users = new ArrayList<>();


    //this will handle the login request
    @RequestMapping(path ="/login",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE) //consumes the JSON send to /login
    public void logIn(@RequestBody User user){
        userService.logIn(user);
    }

    @RequestMapping(path ="/register",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerUser(@RequestBody User user){
        userService.registerUser(user);
    }

}
