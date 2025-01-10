package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.User;

public interface UserService {

    ResponseEntity<?> registration(User user);
    
}
