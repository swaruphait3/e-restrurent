package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.FoodType;

public interface FoodTypeRepository extends JpaRepository<FoodType, Integer>{
    
}
