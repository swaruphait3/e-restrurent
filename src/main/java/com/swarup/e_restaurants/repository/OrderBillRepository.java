package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.OrderBill;

public interface OrderBillRepository extends JpaRepository<OrderBill, Integer>{
    
}
