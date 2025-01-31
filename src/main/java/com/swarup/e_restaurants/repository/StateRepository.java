package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.State;

public interface StateRepository extends JpaRepository<State, Integer>{
    
}
