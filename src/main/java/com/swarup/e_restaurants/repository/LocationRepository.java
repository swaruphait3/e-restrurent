package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
    
}
