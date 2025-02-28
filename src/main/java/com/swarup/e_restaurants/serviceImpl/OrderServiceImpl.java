package com.swarup.e_restaurants.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.FoodCategories;
import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.OrderBill;
import com.swarup.e_restaurants.model.OrderDetails;
import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.FoodCategoriesRepository;
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

    @Autowired
    private FoodCategoriesRepository foodCategoriesRepository;

    @Override
    public ResponseEntity<?> purchase(OrderDetails orderDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> byEmail = userRepositiry.findByEmail(userDetails.getUser().getEmail());
        System.out.println(orderDetails);
        if (byEmail.isPresent()) {
            orderDetails.setOrderStatus("P");
            orderDetails.setCustomerId(byEmail.get().getId());
            orderDetails.setPurchaseDate(LocalDate.now());
            orderDetails.setPurchaseTime(LocalTime.now());
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
       for (OrderDetails orderDetails : allByCustomerId) {
        Optional<FoodCategories> byId = foodCategoriesRepository.findById(orderDetails.getItemId());
        orderDetails.setResturentName(restrurentRepository.findById(orderDetails.getRestId()).get().getName());
        orderDetails.setItemName(byId.get().getName());
        orderDetails.setImages(byId.get().getImages());
       }
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
            for (OrderDetails orderDetails : allByCustomerId) {
                Optional<FoodCategories> byId = foodCategoriesRepository.findById(orderDetails.getItemId());
                orderDetails.setResturentName(restrurentRepository.findById(orderDetails.getRestId()).get().getName());
                orderDetails.setItemName(byId.get().getName());
                orderDetails.setImages(byId.get().getImages());
               }
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
            float netAmount = byId.get().getTotalAmount() * 0.82f;
            float taxAmount = byId.get().getTotalAmount() * 0.18f;
            orderBill.setNet(netAmount);
            orderBill.setTax(taxAmount);
            orderBill.setGross(byId.get().getTotalAmount());
            orderBill.setPayMode(byId.get().getModeOfPayment());
            OrderBill save = orderBillRepository.save(orderBill);
            byId.get().setBillNo(save.getId());
            byId.get().setOrderStatus("A");

            LocalDateTime plusMinutes = LocalDateTime.now().plusMinutes(30);
            byId.get().setExpectedDelivary(plusMinutes);
            // LocalDate localDate = plusMinutes.toLocalDate();
            // LocalTime localTime = plusMinutes.toLocalTime();
            
            // System.out.println("LocalDate: " + localDate);
            // System.out.println("LocalTime: " + localTime);
            // byId.get().setDelivaryDate(localDate);
            orderDetailsRepository.save(byId.get());
       
            return ResponseHandler.generateResponse("Order Confiremd.",HttpStatus.OK, null);   
        } else {
         return ResponseHandler.generateResponse("No valid Details found..",HttpStatus.BAD_REQUEST, null);         
        }
    }
    
}
