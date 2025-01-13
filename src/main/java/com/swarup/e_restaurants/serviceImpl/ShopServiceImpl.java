package com.swarup.e_restaurants.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.Shop;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.ShopRepository;
import com.swarup.e_restaurants.repository.UserRepositiry;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.ShopService;

@Service
public class ShopServiceImpl implements ShopService{


    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepositiry userRepositiry;

    @Override
    public ResponseEntity<?> registation(Shop shop) {
        boolean existsByNameAndContactNoAndEmailAndStatus = shopRepository.existsByNameAndContactNoAndEmailAndStatus(shop.getName(), shop.getContactNo(), shop.getEmail(), true);
       if (!existsByNameAndContactNoAndEmailAndStatus) {
        shop.setStatus(true);
        shopRepository.save(shop);
        boolean existsByEmailOrMobile = userRepositiry.existsByEmailOrMobile(shop.getEmail(), shop.getContactNo());
        if (!existsByEmailOrMobile) {
            User user = new User();
            user.setName(shop.getName());
            user.setEmail(shop.getEmail());
            user.setPassword(passwordEncoder.encode(shop.getRawPass()));
            user.setMobile(shop.getContactNo());
            user.setEnabled(true);
            user.setRole("SHOP");
            userRepositiry.save(user);
        } else {
            Optional<User> byEmailOrMobile = userRepositiry.findByEmailOrMobile(shop.getEmail(),shop.getContactNo());
            User user = byEmailOrMobile.get();
            user.setRole("SHOP");
            userRepositiry.save(user);
        }
        return ResponseHandler.generateResponse("Successfully created Shop",HttpStatus.OK, null);

       } else {
        return ResponseHandler.generateResponse("Shop Alreay Present...",HttpStatus.BAD_REQUEST, null);
       }
    }

    @Override
    public ResponseEntity<?> edit(Shop shop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'edit'");
    }

    @Override
    public ResponseEntity<?> findAll() {
       List<Shop> all = shopRepository.findAll();
       if (all.isEmpty()) {
        return ResponseHandler.generateResponse("No vaild present...",HttpStatus.OK, null);   
       } else {
        return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, all);
        
       }
    }

    @Override
    public ResponseEntity<?> findAllActiveList() {
        List<Shop> all = shopRepository.findAll().stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        if (all.isEmpty()) {
         return ResponseHandler.generateResponse("No vaild present...",HttpStatus.OK, null);   
        } else {
         return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, all);
         
        }
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
      Optional<Shop> byId = shopRepository.findById(id);
      if (byId.isPresent()) {
        return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
      } else {
        return ResponseHandler.generateResponse("No vaild present...",HttpStatus.BAD_REQUEST, null);   
        
      }
    }

    @Override
    public ResponseEntity<?> activeShop(Integer id) {
        Optional<Shop> byId = shopRepository.findById(id);
        if (!byId.get().isStatus()) {
            Shop shop = byId.get();
            shop.setStatus(false);
            shopRepository.save(shop);
            Optional<User> byEmailOrMobile = userRepositiry.findByEmailOrMobile(shop.getEmail(), shop.getContactNo());
            byEmailOrMobile.get().setEnabled(false);
            userRepositiry.save(byEmailOrMobile.get());
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
        } else {
          return ResponseHandler.generateResponse("Alreay Active...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }

    @Override
    public ResponseEntity<?> deActiveShop(Integer id) {
        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.get().isStatus()) {
            Shop shop = byId.get();
            shop.setStatus(true);
            shopRepository.save(shop);
            Optional<User> byEmailOrMobile = userRepositiry.findByEmailOrMobile(shop.getEmail(), shop.getContactNo());
            byEmailOrMobile.get().setEnabled(true);
            userRepositiry.save(byEmailOrMobile.get());
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
        } else {
          return ResponseHandler.generateResponse("Alreay Deactive...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }
    
}
