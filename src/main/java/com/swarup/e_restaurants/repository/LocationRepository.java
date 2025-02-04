package com.swarup.e_restaurants.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{

   List<Location> findAllByCityId(int cityId);
    
}
