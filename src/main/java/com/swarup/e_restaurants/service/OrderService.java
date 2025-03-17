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

    ResponseEntity<?> readyOrder(int id);

    ResponseEntity<?> delivaryOrder(int id);

    ResponseEntity<?> viewRestrurentWiseBill();

    ResponseEntity<?> viewRestrurentWiseBillDateWise(String startDate, String endDate);

    ResponseEntity<?> pickOrderForDelivaryBoy();

    ResponseEntity<?> viewOrderListDelivaryBoy();

    
}
