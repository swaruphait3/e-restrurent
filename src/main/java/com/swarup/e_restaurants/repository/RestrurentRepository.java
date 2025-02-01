package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Restrurent;

public interface RestrurentRepository extends JpaRepository<Restrurent, Integer> {
    
   boolean existsByNameAndEmail(String name, String email);
}
