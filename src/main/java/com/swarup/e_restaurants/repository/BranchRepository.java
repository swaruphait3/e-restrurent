package com.swarup.e_restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swarup.e_restaurants.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Integer>{

   boolean existsByBranchNameAndBranchEmail(String branchName, String branchEmail);
    
}
