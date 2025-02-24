package com.swarup.e_restaurants.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.OrderBill;
import com.swarup.e_restaurants.model.OrderDetails;
import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.OrderBillRepository;
import com.swarup.e_restaurants.repository.OrderDetailsRepository;
import com.swarup.e_restaurants.repository.RestrurentRepository;
import com.swarup.e_restaurants.repository.UserRepositiry;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;


    @Autowired
    private OrderBillRepository orderBillRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private RestrurentRepository restrurentRepository;

    @Override
    public ResponseEntity<?> purchase(OrderDetails orderDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> byEmail = userRepositiry.findByEmail(userDetails.getUser().getEmail());
        System.out.println(orderDetails);
        if (byEmail.isPresent()) {
            orderDetails.setOrderStatus("P");
            orderDetails.setCustomerId(byEmail.get().getId());
            orderDetailsRepository.save(orderDetails);
         return ResponseHandler.generateResponse("Successfully Placed Your Order..",HttpStatus.OK, null);

        } else {
         return ResponseHandler.generateResponse("No valid User found..",HttpStatus.BAD_REQUEST, null);

        }

    }

    @Override
    public ResponseEntity<?> viewOrderPurchaser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> byEmail = userRepositiry.findByEmail(userDetails.getUser().getEmail());
        if (byEmail.isPresent()) {
            List<OrderDetails> allByCustomerId = orderDetailsRepository.findAllByCustomerId(byEmail.get().getId());

       return ResponseHandler.generateResponse("Data Featch..",HttpStatus.OK, allByCustomerId);
            
        } else {
         return ResponseHandler.generateResponse("No valid User found..",HttpStatus.BAD_REQUEST, null);
            
        }
    }

    @Override
    public ResponseEntity<?> viewOrderRestaurant() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
        if (byEmail.isPresent()) {
            List<OrderDetails> allByCustomerId = orderDetailsRepository.findAllByRestId(byEmail.get().getId());
            return ResponseHandler.generateResponse("Data Featch..",HttpStatus.OK, allByCustomerId);   
        } else {
         return ResponseHandler.generateResponse("No valid User found..",HttpStatus.BAD_REQUEST, null);   
        }
    }

    @Override
    public ResponseEntity<?> findById(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Data Featch..",HttpStatus.OK, byId);   
        } else {
         return ResponseHandler.generateResponse("No valid Details found..",HttpStatus.BAD_REQUEST, null);         
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setOrderStatus("C");
            orderDetailsRepository.save(byId.get());
            return ResponseHandler.generateResponse("Order Canceled..",HttpStatus.OK, null);   
        } else {
         return ResponseHandler.generateResponse("No valid Details found..",HttpStatus.BAD_REQUEST, null);         
        }
    }

    @Override
    public ResponseEntity<?> approveOrder(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            OrderBill orderBill = new OrderBill();
            orderBill.setBillDate(LocalDate.now());
            orderBill.setRestName(restrurentRepository.findById(byId.get().getRestId()).get().getName());
            orderBill.setCustName(userRepositiry.findById(byId.get().getCustomerId()).get().getName());
            orderBill.setCustAddress(byId.get().getDelivaryAddress());
            orderBill.setCustContactNo(byId.get().getContactNo());
            OrderBill save = orderBillRepository.save(orderBill);
            byId.get().setBillNo(save.getId());
            byId.get().setOrderStatus("A");
            orderDetailsRepository.save(byId.get());
       
            return ResponseHandler.generateResponse("Order Canceled..",HttpStatus.OK, null);   
        } else {
         return ResponseHandler.generateResponse("No valid Details found..",HttpStatus.BAD_REQUEST, null);         
        }
    }
    
}
