package com.swarup.e_restaurants.service;

import org.springframework.http.ResponseEntity;

import com.swarup.e_restaurants.model.Branch;
import com.swarup.e_restaurants.model.dto.BranchMaltipartForm;

public interface BranchService {

    ResponseEntity<?> add(Branch branch);

    ResponseEntity<?> edit(Branch branch);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAllActiveList();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> active(Integer id);

    ResponseEntity<?> deActive(Integer id);

    ResponseEntity<?> addBranch(BranchMaltipartForm branchForm);
    
}
