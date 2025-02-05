package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.model.dto.FoodMultipartForm;
import com.swarup.e_restaurants.service.FoodItemService;

@RestController
@RequestMapping("/food")
public class FoodItemController {
    
    @Autowired
    private FoodItemService foodItemService; 

    
      @PostMapping(value = "/add")
    public ResponseEntity<?> add(@ModelAttribute FoodMultipartForm multipartForm ) {
        return foodItemService.add(multipartForm);
    }

     @PutMapping(value = "/edit")
    public ResponseEntity<?> edit(@ModelAttribute FoodMultipartForm multipartForm) {
        return foodItemService.edit(multipartForm);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        return foodItemService.findAll();
    }

    @GetMapping(value = "/findAllActiveList")
    public ResponseEntity<?> findAllActiveList() {
        return foodItemService.findAllActiveList();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return foodItemService.findById(id);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> active(@RequestParam Integer id) {
        return foodItemService.active(id);
    }

    @GetMapping(value = "/deActive")
    public ResponseEntity<?> deActive(@RequestParam Integer id) {
        return foodItemService.deActive(id);
    }
}
