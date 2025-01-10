package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByEmailOrPhone(String email, String phone);
    
}
