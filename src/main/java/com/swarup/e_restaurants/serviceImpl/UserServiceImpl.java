package com.swarup.e_restaurants.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.UserRepositiry;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> add(User user) {
        boolean existsByEmailOrMobile = userRepositiry.existsByEmailOrMobile(user.getEmail(), user.getMobile());

        if (existsByEmailOrMobile) {
           return ResponseHandler.generateResponse("You Have Already account this email or mobile number",HttpStatus.BAD_REQUEST, null);   
        } else {
            user.setEnabled(true);
            user.setRole("USER");
            user.setPassword(passwordEncoder.encode(user.getRawPassword()));
            userRepositiry.save(user);
           return ResponseHandler.generateResponse("Successfully Register",HttpStatus.OK, null);   
        }

    }

    @Override
    public ResponseEntity<?> edit(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'edit'");
    }

    @Override
    public ResponseEntity<?> findAll() {
       List<User> all = userRepositiry.findAll().stream().filter(u -> u.getRole().equals("USER")).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully User fetched",HttpStatus.OK, all);   
    }

    @Override
    public ResponseEntity<?> findAllActiveUserList() {
        List<User> all = userRepositiry.findAll().stream().filter(u -> u.getRole().equals("USER") && u.isEnabled()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully User fetched",HttpStatus.OK, all);   
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
     Optional<User> user= userRepositiry.findById(id);
     if (user.isPresent()) {
        return ResponseHandler.generateResponse("fetched User Details",HttpStatus.OK, user);   
     } else {
        return ResponseHandler.generateResponse("Opps. something went wrong",HttpStatus.BAD_REQUEST, null);   
        
     }
    }

    @Override
    public ResponseEntity<?> active(Integer id) {
        Optional<User> user= userRepositiry.findById(id);
        if (!user.get().isEnabled()) {
            user.get().setEnabled(true);
            userRepositiry.save(user.get());
        return ResponseHandler.generateResponse("Successfully Activate..",HttpStatus.OK, null);   

        } else {
        return ResponseHandler.generateResponse("Opps. user already active",HttpStatus.BAD_REQUEST, null);   
            
        }
    }

    @Override
    public ResponseEntity<?> deActive(Integer id) {
        Optional<User> user= userRepositiry.findById(id);
        if (user.get().isEnabled()) {
            user.get().setEnabled(false);
            userRepositiry.save(user.get());
        return ResponseHandler.generateResponse("Successfully Deactive..",HttpStatus.OK, null);   

        } else {
        return ResponseHandler.generateResponse("Opps. user already Deactive",HttpStatus.BAD_REQUEST, null);   
            
        }
    }

    @Override
    public ResponseEntity<?> findAllDelivaryBoy() {
       List<User> collect = userRepositiry.findAll().stream().filter(u -> u.getRole().equals("DBOY")).collect(Collectors.toList());
       return ResponseHandler.generateResponse("Successfully User fetched",HttpStatus.OK, collect);   
    }

    @Override
    public ResponseEntity<?> addDelivaryBoy(User user) {
        boolean existsByEmailOrMobile = userRepositiry.existsByEmailOrMobile(user.getEmail(), user.getMobile());

        if (existsByEmailOrMobile) {
           return ResponseHandler.generateResponse("You Have Already account this email or mobile number",HttpStatus.BAD_REQUEST, null);   
        } else {
            user.setEnabled(true);
            user.setRole("DBOY");
            user.setPassword(passwordEncoder.encode(user.getRawPassword()));
            userRepositiry.save(user);
           return ResponseHandler.generateResponse("Successfully Register",HttpStatus.OK, null);   
        }

    }

    @Override
    public ResponseEntity<?> findActiveAllDelivaryBoy() {
        List<User> collect = userRepositiry.findAll().stream().filter(u -> u.getRole().equals("DBOY") && u.isEnabled()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully User fetched",HttpStatus.OK, collect);   
    }
    
}
