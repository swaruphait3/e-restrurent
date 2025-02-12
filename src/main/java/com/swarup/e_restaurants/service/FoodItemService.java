package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.dto.FoodMultipartForm;

public interface FoodItemService {

    ResponseEntity<?> add(FoodMultipartForm multipartForm);

    ResponseEntity<?> edit(FoodMultipartForm multipartForm);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAllActiveList();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> active(Integer id);

    ResponseEntity<?> deActive(Integer id);

    ResponseEntity<?> findItemsByResturentId(Integer id);
    
}
