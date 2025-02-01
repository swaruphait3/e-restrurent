package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.FoodCategories;

public interface FoodCategoriesRepository extends JpaRepository<FoodCategories, Integer>{
    
}
