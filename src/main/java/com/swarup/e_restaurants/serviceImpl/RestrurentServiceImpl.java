package com.swarup.e_restaurants.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.RestrurentRepository;
import com.swarup.e_restaurants.repository.UserRepositiry;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.RestrurentService;

@Service
public class RestrurentServiceImpl implements RestrurentService{

    @Autowired
    private RestrurentRepository restrurentRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> add(Restrurent restrurent) {
        boolean existsByNameAndEmail = restrurentRepository.existsByNameAndEmail(restrurent.getName(), restrurent.getEmail());
        if (!existsByNameAndEmail) {
            restrurent.setStatus(true);
            restrurentRepository.save(restrurent);
            boolean existsByEmail = userRepositiry.existsByEmail(restrurent.getEmail());
            if (!existsByEmail) {
                User user = new User();
               user.setName(restrurent.getName());
               user.setEmail(restrurent.getEmail());
            user.setPassword(passwordEncoder.encode(restrurent.getPassword()));
            user.setMobile(restrurent.getContact());
            user.setEnabled(true);
            user.setRole("RESTAURENT");
            userRepositiry.save(user);
            }else {
                Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restrurent.getEmail());
                User user = byEmailOrMobile.get();
                user.setRole("RESTAURENT");
                userRepositiry.save(user);
            }
        return ResponseHandler.generateResponse("Successfully created Restrurent",HttpStatus.OK, null);

        }  else {
        return ResponseHandler.generateResponse("Shop Alreay Present...",HttpStatus.BAD_REQUEST, null);
       }
    }

    @Override
    public ResponseEntity<?> edit(Restrurent restrurent) {
        restrurentRepository.save(restrurent);
        return ResponseHandler.generateResponse("Successfully Updated Restrurent",HttpStatus.OK, null);

    };

    @Override
    public ResponseEntity<?> findAll() {
        List<Restrurent> all = restrurentRepository.findAll();
        if (all.isEmpty()) {
         return ResponseHandler.generateResponse("No vaild present...",HttpStatus.OK, null);   
        } else {
         return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, all);
         
        }
    }

    @Override
    public ResponseEntity<?> findAllActiveList() {
         List<Restrurent> all = restrurentRepository.findAll().stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        if (all.isEmpty()) {
         return ResponseHandler.generateResponse("No vaild present...",HttpStatus.OK, null);   
        } else {
         return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, all);
        }
         
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<Restrurent> byId = restrurentRepository.findById(id);
        if (byId.isPresent()) {
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, byId);   
        } else {
          return ResponseHandler.generateResponse("No vaild present...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }

    @Override
    public ResponseEntity<?> active(Integer id) {
        Optional<Restrurent> byId = restrurentRepository.findById(id);
        if (!byId.get().isStatus()) {
            Restrurent restrurent = byId.get();
            restrurent.setStatus(false);
            restrurentRepository.save(restrurent);
            Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restrurent.getEmail());
            byEmailOrMobile.get().setEnabled(false);
            userRepositiry.save(byEmailOrMobile.get());
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
        } else {
          return ResponseHandler.generateResponse("Alreay Deactive...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }

    @Override
    public ResponseEntity<?> deActive(Integer id) {
        Optional<Restrurent> byId = restrurentRepository.findById(id);
        if (byId.get().isStatus()) {
            Restrurent restrurent = byId.get();
            restrurent.setStatus(true);
            restrurentRepository.save(restrurent);
            Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restrurent.getEmail());
            byEmailOrMobile.get().setEnabled(true);
            userRepositiry.save(byEmailOrMobile.get());
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
        } else {
          return ResponseHandler.generateResponse("Alreay Deactive...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }
    
}
