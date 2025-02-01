package com.swarup.e_restaurants.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.User;


public interface UserRepositiry extends JpaRepository<User, Long> {

Optional<User> findByEmailOrMobile(String email, String mobile);

Optional<User> findByEmail(String email);

boolean existsByEmailOrMobile(String email, String mobile);

boolean existsByEmail(String email);

}
