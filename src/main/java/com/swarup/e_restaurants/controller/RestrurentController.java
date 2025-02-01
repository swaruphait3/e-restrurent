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

import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.service.RestrurentService;

@RestController
@RequestMapping("/restrurent")
public class RestrurentController {
    
   @Autowired
   private RestrurentService restrurentService;

   @PostMapping(value = "/add")
    public ResponseEntity<?> add(@RequestBody Restrurent restrurent ) {
        return restrurentService.add(restrurent);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> edit(@RequestBody Restrurent restrurent) {
        return restrurentService.edit(restrurent);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        return restrurentService.findAll();
    }

    @GetMapping(value = "/findAllActiveList")
    public ResponseEntity<?> findAllActiveList() {
        return restrurentService.findAllActiveList();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return restrurentService.findById(id);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> active(@RequestParam Integer id) {
        return restrurentService.active(id);
    }

    @GetMapping(value = "/deActive")
    public ResponseEntity<?> deActive(@RequestParam Integer id) {
        return restrurentService.deActive(id);
    }
}
