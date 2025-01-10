package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
   
    @Autowired
    private UserService userService;

     @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody User user) {
        return userService.registration(user);
    }
}
