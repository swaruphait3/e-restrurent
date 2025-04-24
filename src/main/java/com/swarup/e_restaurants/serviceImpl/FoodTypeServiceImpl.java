package com.swarup.e_restaurants.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.FoodType;
import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.repository.FoodTypeRepository;
import com.swarup.e_restaurants.repository.RestrurentRepository;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.FoodTypeService;

@Service
public class FoodTypeServiceImpl implements FoodTypeService{

@Autowired
private FoodTypeRepository foodTypeRepository;

@Autowired
private RestrurentRepository restrurentRepository;

    @Override
    public ResponseEntity<?> add(FoodType foodType) {
              Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        Optional<Restrurent> resturent = restrurentRepository.findByEmail(userDetails.getUser().getEmail());

        if (resturent.isPresent()) {
            foodType.setRestId(resturent.get().getId());
            foodType.setStatus(true);
            foodTypeRepository.save(foodType);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Saved....");

        } else {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No valid Restrurent found..");

            
        }


        
    }

    @Override
    public ResponseEntity<?> edit(FoodType foodType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'edit'");
    }

    @Override
    public ResponseEntity<?> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        Optional<Restrurent> resturent = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
        if (resturent.isPresent()) {

        List<FoodType> all = foodTypeRepository.findAllByRestId(resturent.get().getId());

          if (all.isEmpty()) {
            return ResponseHandler.generateResponse("No vaild present...",HttpStatus.OK, null);   
           } else {
               for (FoodType foodType : all) {
                   foodType.setResturent(restrurentRepository.findById(foodType.getRestId()).get().getName());
               }
            return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, all);
            
           }
        } else {
         return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, null);
          
        }
 
    }

    @Override
    public ResponseEntity<?> findAllActiveList() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        Optional<Restrurent> resturent = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
        if (resturent.isPresent()) {

         List<FoodType> all = foodTypeRepository.findAllByRestId(resturent.get().getId()).stream().filter(t -> t.isStatus()).collect(Collectors.toList());
       
         return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, all);
        
        }
        else {
         return ResponseHandler.generateResponse("Successful fetch Data...",HttpStatus.OK, null);
          
        }
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<FoodType> byId = foodTypeRepository.findById(id);
        if (byId.isPresent()) {
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, byId);   
        } else {
          return ResponseHandler.generateResponse("No vaild present...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }

    @Override
    public ResponseEntity<?> active(Integer id) {
        Optional<FoodType> byId = foodTypeRepository.findById(id);
        if (!byId.get().isStatus()) {
            FoodType foodType = byId.get();
            foodType.setStatus(true);
            foodTypeRepository.save(foodType);
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
        } else {
          return ResponseHandler.generateResponse("Alreay Deactive...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }

    @Override
    public ResponseEntity<?> deActive(Integer id) {
       Optional<FoodType> byId = foodTypeRepository.findById(id);
        if (byId.get().isStatus()) {
            FoodType foodType = byId.get();
            foodType.setStatus(false);
            foodTypeRepository.save(foodType);
          return ResponseHandler.generateResponse("Successful fetch the details...",HttpStatus.OK, null);   
        } else {
          return ResponseHandler.generateResponse("Alreay Deactive...",HttpStatus.BAD_REQUEST, null);   
          
        }
    }
    
}
