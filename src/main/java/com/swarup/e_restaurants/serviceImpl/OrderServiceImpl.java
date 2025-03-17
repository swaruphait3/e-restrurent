package com.swarup.e_restaurants.serviceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class OrderServiceImpl implements OrderService {

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
            return ResponseHandler.generateResponse("Successfully Placed Your Order..", HttpStatus.OK, null);

        } else {
            return ResponseHandler.generateResponse("No valid User found..", HttpStatus.BAD_REQUEST, null);

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
            return ResponseHandler.generateResponse("Data Featch..", HttpStatus.OK, allByCustomerId);

        } else {
            return ResponseHandler.generateResponse("No valid User found..", HttpStatus.BAD_REQUEST, null);

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
            return ResponseHandler.generateResponse("Data Featch..", HttpStatus.OK, allByCustomerId);
        } else {
            return ResponseHandler.generateResponse("No valid User found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> findById(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Data Featch..", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setOrderStatus("C");
            orderDetailsRepository.save(byId.get());
            return ResponseHandler.generateResponse("Order Canceled..", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
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
            float netAmount = byId.get().getTotalAmount() * 0.90f;
            float taxAmount = byId.get().getTotalAmount() * 0.10f;
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

            return ResponseHandler.generateResponse("Order Confiremd.", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> viewBill(int id) {
        // Optional<OrderBill> byId = orderBillRepository.findById(id);
        // if (byId.isPresent()) {
        // return ResponseHandler.generateResponse("Fetched The
        // Details...",HttpStatus.OK, byId);
        // } else {
        // return ResponseHandler.generateResponse("Order
        // Confiremd.",HttpStatus.BAD_REQUEST, null);
        // }
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            Optional<FoodCategories> byId1 = foodCategoriesRepository.findById(byId.get().getItemId());
            byId.get().setItemName(byId1.get().getName());
            return ResponseHandler.generateResponse("Fetched The Details...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> readyOrder(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setOrderStatus("R");
            orderDetailsRepository.save(byId.get());
            Optional<OrderBill> byId2 = orderBillRepository.findById(byId.get().getBillNo());
            if (byId2.isPresent()) {
                OrderBill orderBill = byId2.get();
                orderBill.setDelivaryStatus("R");
                orderBillRepository.save(orderBill);
            }
            return ResponseHandler.generateResponse("Order Ready..", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> delivaryOrder(int id) {
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setOrderStatus("D");
            byId.get().setDelivaryDate(LocalDate.now());
            byId.get().setDelivaryTime(LocalTime.now());
            orderDetailsRepository.save(byId.get());
            Optional<OrderBill> byId2 = orderBillRepository.findById(byId.get().getBillNo());
            if (byId2.isPresent()) {
                OrderBill orderBill = byId2.get();
                orderBill.setDelivaryStatus("D");
                orderBillRepository.save(orderBill);
            }
            return ResponseHandler.generateResponse("Order Delivared..", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> viewRestrurentWiseBill() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
        if (byEmail.isPresent()) {
            List<OrderDetails> allByCustomerId = orderDetailsRepository.findAllByRestId(byEmail.get().getId()).stream()
                    .filter(t -> t.getBillNo() != 0).collect(Collectors.toList());
            for (OrderDetails orderDetails : allByCustomerId) {
                Optional<FoodCategories> byId = foodCategoriesRepository.findById(orderDetails.getItemId());
                Optional<OrderBill> byId2 = orderBillRepository.findById(orderDetails.getBillNo());
                orderDetails.setOrderBill(byId2.get());
                orderDetails.setPlatformCharge((orderDetails.getTotalAmount() * 0.10f));
                orderDetails.setNetEarning((orderDetails.getTotalAmount() * 0.90f));
                orderDetails.setItemName(byId.get().getName());
                orderDetails.setNet(byId2.get().getNet());
                orderDetails.setTax(byId2.get().getTax());
                orderDetails.setGross(byId2.get().getGross());
            }
            return ResponseHandler.generateResponse("Data Featch..", HttpStatus.OK, allByCustomerId);
        } else {
            return ResponseHandler.generateResponse("No valid User found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> viewRestrurentWiseBillDateWise(String startDate, String endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(startDate);
        Instant nowUtc2 = Instant.parse(endDate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();

        Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
        if (byEmail.isPresent()) {
            System.out.println("byEmail.get().getId()" + byEmail.get().getId());

            List<OrderDetails> allByCustomerId = orderDetailsRepository.fetchResturantWiseSale(byEmail.get().getId(),
                    stdt.toString(), eddt.toString());

            for (OrderDetails orderDetails : allByCustomerId) {
                Optional<FoodCategories> byId = foodCategoriesRepository.findById(orderDetails.getItemId());
                Optional<OrderBill> byId2 = orderBillRepository.findById(orderDetails.getBillNo());
                orderDetails.setOrderBill(byId2.get());
                orderDetails.setPlatformCharge((orderDetails.getTotalAmount() * 0.10f));
                orderDetails.setNetEarning((orderDetails.getTotalAmount() * 0.90f));
                orderDetails.setItemName(byId.get().getName());
                orderDetails.setNet(byId2.get().getNet());
                orderDetails.setTax(byId2.get().getTax());
                orderDetails.setGross(byId2.get().getGross());
            }
            return ResponseHandler.generateResponse("Data Featch..", HttpStatus.OK, allByCustomerId);
        } else {
            return ResponseHandler.generateResponse("No valid User found..", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> pickOrderForDelivaryBoy() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        List<OrderDetails> fetchOpenOrderForPickDelivary = orderDetailsRepository
                .fetchOpenOrderForPickDelivary(userDetails.getUser().getId());
        for (OrderDetails orderDetails : fetchOpenOrderForPickDelivary) {
            Optional<Restrurent> byId = restrurentRepository.findById(orderDetails.getRestId());
            Optional<FoodCategories> byIds = foodCategoriesRepository.findById(orderDetails.getItemId());
           if (byId.isPresent()) {
            orderDetails.setResturentName(byId.get().getName());
            orderDetails.setRestAddress(byId.get().getAddress());
            orderDetails.setItemName(byIds.get().getName());
           }
        }
        return ResponseHandler.generateResponse("Data Featch..", HttpStatus.OK, fetchOpenOrderForPickDelivary);
    }

    @Override
    public ResponseEntity<?> pickOrderDelivaryBoy(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<OrderDetails> byId = orderDetailsRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setOrderStatus("W");
            byId.get().setDelivaryPersonId(userDetails.getUser().getId());
            LocalDateTime plusMinutes = LocalDateTime.now().plusMinutes(30);
            byId.get().setExpectedDelivary(plusMinutes);
            orderDetailsRepository.save(byId.get());
            return ResponseHandler.generateResponse("Order Pick to delivary..", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("No valid Details found..", HttpStatus.BAD_REQUEST, null);
        }
    }



}
