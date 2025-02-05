package com.swarup.e_restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swarup.e_restaurants.model.FoodType;
import com.swarup.e_restaurants.service.FoodTypeService;

@RestController
@RequestMapping("/foodtype")
public class FoodTypeController {
    
    @Autowired
    private FoodTypeService foodTypeService;

      @PostMapping(value = "/add")
    public ResponseEntity<?> add(@RequestBody FoodType foodType ) {
        return foodTypeService.add(foodType);
    }

     @PutMapping(value = "/edit")
    public ResponseEntity<?> edit(@RequestBody FoodType foodType) {
        return foodTypeService.edit(foodType);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        return foodTypeService.findAll();
    }

    @GetMapping(value = "/findAllActiveList")
    public ResponseEntity<?> findAllActiveList() {
        return foodTypeService.findAllActiveList();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return foodTypeService.findById(id);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> active(@RequestParam Integer id) {
        return foodTypeService.active(id);
    }

    @GetMapping(value = "/deActive")
    public ResponseEntity<?> deActive(@RequestParam Integer id) {
        return foodTypeService.deActive(id);
    }
}
