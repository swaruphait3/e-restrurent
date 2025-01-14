package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    
}
