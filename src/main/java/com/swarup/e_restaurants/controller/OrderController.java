package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.model.OrderDetails;
import com.swarup.e_restaurants.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/purchase")
    public ResponseEntity<?> purchase(@RequestBody OrderDetails orderDetails ) {
        return orderService.purchase(orderDetails);
    }

    @GetMapping(value = "/viewOrderPurchaser")
    public ResponseEntity<?> viewOrderPurchaser() {
        return orderService.viewOrderPurchaser();
    }

    @GetMapping(value = "/viewOrderRestaurant")
    public ResponseEntity<?> viewOrderRestaurant() {
        return orderService.viewOrderRestaurant();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam int id) {
        return orderService.findById(id);
    }

    @GetMapping(value = "/cancelOrder")
    public ResponseEntity<?> cancelOrder(@RequestParam int id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping(value = "/approveOrder")
    public ResponseEntity<?> approveOrder(@RequestParam int id) {
        return orderService.approveOrder(id);
    }

    @GetMapping(value = "/viewBill")
    public ResponseEntity<?> viewBill(@RequestParam int id) {
        return orderService.viewBill(id);
    }

}
