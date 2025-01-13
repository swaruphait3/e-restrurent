package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.Shop;

public interface ShopService {

    ResponseEntity<?> registation(Shop shop);

    ResponseEntity<?> edit(Shop shop);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAllActiveList();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeShop(Integer id);

    ResponseEntity<?> deActiveShop(Integer id);
    
}
