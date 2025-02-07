package com.swarup.e_restaurants.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.LocationMaster;


public interface LocationRepository extends JpaRepository<LocationMaster, Integer>{

   List<LocationMaster> findAllByCityId(int cityId);
    
}
