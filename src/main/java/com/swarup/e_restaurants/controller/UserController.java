package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
     @Autowired
   private UserService userService;

   @PostMapping(value = "/add")
    public ResponseEntity<?> add(@RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> edit(@RequestBody User user) {
        return userService.edit(user);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/findAllActiveUserList")
    public ResponseEntity<?> findAllActiveUserList() {
        return userService.findAllActiveUserList();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return userService.findById(id);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> active(@RequestParam Integer id) {
        return userService.active(id);
    }

    @GetMapping(value = "/deActive")
    public ResponseEntity<?> deActive(@RequestParam Integer id) {
        return userService.deActive(id);
    }
}
