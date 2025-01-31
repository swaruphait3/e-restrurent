package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.City;

public interface CityRepository extends JpaRepository<City, Integer>{
    
}
