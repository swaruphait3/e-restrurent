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

import com.swarup.e_restaurants.model.Branch;
import com.swarup.e_restaurants.service.BranchService;

@RestController
@RequestMapping("/branch")
public class BranchController {
    
   @Autowired
   private BranchService branchService;

   @PostMapping(value = "/add")
    public ResponseEntity<?> add(@RequestBody Branch branch ) {
        return branchService.add(branch);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> edit(@RequestBody Branch branch) {
        return branchService.edit(branch);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        return branchService.findAll();
    }

    @GetMapping(value = "/findAllActiveList")
    public ResponseEntity<?> findAllActiveList() {
        return branchService.findAllActiveList();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return branchService.findById(id);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> active(@RequestParam Integer id) {
        return branchService.active(id);
    }

    @GetMapping(value = "/deActive")
    public ResponseEntity<?> deActive(@RequestParam Integer id) {
        return branchService.deActive(id);
    }
}
