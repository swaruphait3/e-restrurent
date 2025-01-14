package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.service.LocationService;

@RestController
@RequestMapping("/lccation")
public class LocationController {
    
    @Autowired
   private LocationService locationService;

    @GetMapping(value = "/findAllCountry")
    public ResponseEntity<?> findAllCountry() {
        return locationService.findAllCountry();
    }
}
