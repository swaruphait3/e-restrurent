package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.OrderDetails;
import java.util.List;


public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer>{
    
    List<OrderDetails> findAllByCustomerId(int customerId);

    List<OrderDetails> findAllByRestId(int restId);
}
