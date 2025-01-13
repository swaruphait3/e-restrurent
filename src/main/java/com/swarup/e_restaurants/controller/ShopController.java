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

import com.swarup.e_restaurants.model.Shop;
import com.swarup.e_restaurants.service.ShopService;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

   @PostMapping(value = "/registation")
    public ResponseEntity<?> registation(@RequestBody Shop shop ) {
        return shopService.registation(shop);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> edit(@RequestBody Shop shop ) {
        return shopService.edit(shop);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        return shopService.findAll();
    }

    @GetMapping(value = "/findAllActiveList")
    public ResponseEntity<?> findAllActiveList() {
        return shopService.findAllActiveList();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return shopService.findById(id);
    }

    @GetMapping(value = "/activeShop")
    public ResponseEntity<?> activeShop(@RequestParam Integer id) {
        return shopService.activeShop(id);
    }

    @GetMapping(value = "/deActiveShop")
    public ResponseEntity<?> deActiveShop(@RequestParam Integer id) {
        return shopService.deActiveShop(id);
    }
}
