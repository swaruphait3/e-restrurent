package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.FoodType;

public interface FoodTypeService {

    ResponseEntity<?> add(FoodType foodType);

    ResponseEntity<?> edit(FoodType foodType);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAllActiveList();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> active(Integer id);

    ResponseEntity<?> deActive(Integer id);
    
}
