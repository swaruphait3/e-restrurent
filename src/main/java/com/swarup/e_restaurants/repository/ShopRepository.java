package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer>{

boolean existsByNameAndContactNoAndEmailAndStatus(String name, String contactNo, String email, boolean status);

}
