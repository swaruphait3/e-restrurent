package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.model.City;
import com.swarup.e_restaurants.model.Location;
import com.swarup.e_restaurants.service.LocationService;



@RestController
@RequestMapping("/location")
public class LocationController {
    
    @Autowired
   private LocationService locationService;

    @GetMapping(value = "/findAllCountry")
    public ResponseEntity<?> findAllCountry() {
        return locationService.findAllCountry();
    }

    @PostMapping("addCity")
    public ResponseEntity<?> addCity(@RequestBody City city) {
        return locationService.addCity(city);
    }
    
    @GetMapping("fetchAllCity")
    public ResponseEntity<?> fetchAllCity() {
        return locationService.fetchAllCity();
    }

    @GetMapping("findCityById")
    public ResponseEntity<?> findCityById(@RequestParam int id) {
        return locationService.findCityById(id);
    }

    @PostMapping("addLocation")
    public ResponseEntity<?> addLocation(@RequestBody Location location) {
        return locationService.addLocation(location);
    }

    @GetMapping("fetchAllLocation")
    public ResponseEntity<?> fetchAllLocation() {
        return locationService.fetchAllLocation();
    }

    @GetMapping("findLocationById")
    public ResponseEntity<?> findLocationById(@RequestParam int id) {
        return locationService.findLocationById(id);
    }
    
}
