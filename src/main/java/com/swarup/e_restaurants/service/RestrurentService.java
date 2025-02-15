package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.dto.RestaurantMaltipartForm;

public interface RestrurentService {

    ResponseEntity<?> add(Restrurent restrurent);

    ResponseEntity<?> edit(Restrurent restrurent);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAllActiveList();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> active(Integer id);

    ResponseEntity<?> deActive(Integer id);

    ResponseEntity<?> addResaturant(RestaurantMaltipartForm restaurantMaltipartForm);
    
}
