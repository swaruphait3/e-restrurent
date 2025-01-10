package com.swarup.e_restaurants.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.UserRepository;
import com.swarup.e_restaurants.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> registration(User user) {
       if (user.getRole()==null) {
         user.setRole("USER");
       }
       user.setEnabled(true);
       userRepository.save(user);
       return ResponseEntity.ok().body("Successfully Saved...");
    }
    
}
