package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.OrderDetails;

public interface OrderService {

    ResponseEntity<?> purchase(OrderDetails orderDetails);

    ResponseEntity<?> viewOrderPurchaser();

    ResponseEntity<?> viewOrderRestaurant();

    ResponseEntity<?> findById(int id);

    ResponseEntity<?> cancelOrder(int id);

    ResponseEntity<?> approveOrder(int id);

    ResponseEntity<?> viewBill(int id);
    
}
