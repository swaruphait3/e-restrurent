package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.City;
import com.swarup.e_restaurants.model.Location;

public interface LocationService {

    ResponseEntity<?> findAllCountry();

    ResponseEntity<?> addCity(City city);

    ResponseEntity<?> fetchAllCity();

    ResponseEntity<?> findCityById(int id);

    ResponseEntity<?> addLocation(Location location);

    ResponseEntity<?> fetchAllLocation();

    ResponseEntity<?> findLocationById(int id);
    
}
