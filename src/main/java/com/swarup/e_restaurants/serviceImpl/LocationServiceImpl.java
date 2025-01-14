package com.swarup.e_restaurants.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.Country;
import com.swarup.e_restaurants.repository.CountryRepository;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService{

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public ResponseEntity<?> findAllCountry() {
      List<Country> all = countryRepository.findAll();
      if (all.isEmpty()) {
        return ResponseHandler.generateResponse("No Country Prsent",HttpStatus.OK, null);
      } else {
        return ResponseHandler.generateResponse("Successfully country fetch",HttpStatus.OK, all);
      }
    }
    
}
