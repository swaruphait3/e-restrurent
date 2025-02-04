package com.swarup.e_restaurants.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Restrurent;


public interface RestrurentRepository extends JpaRepository<Restrurent, Integer> {
    
   boolean existsByNameAndEmail(String name, String email);

   Optional<Restrurent> findByEmail(String email);
}
