package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.User;

public interface UserService {

    ResponseEntity<?> add(User user);

    ResponseEntity<?> edit(User user);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAllActiveUserList();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> active(Integer id);

    ResponseEntity<?> deActive(Integer id);

    ResponseEntity<?> findAllDelivaryBoy();

    ResponseEntity<?> addDelivaryBoy(User user);

    ResponseEntity<?> findActiveAllDelivaryBoy();
    
}
