package com.swarup.e_restaurants.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.FoodType;

public interface FoodTypeRepository extends JpaRepository<FoodType, Integer>{
    

   List<FoodType> findAllByRestId(int restId);
}
