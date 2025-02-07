package com.swarup.e_restaurants.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.City;
import com.swarup.e_restaurants.model.Country;
import com.swarup.e_restaurants.model.LocationMaster;
import com.swarup.e_restaurants.repository.CityRepository;
import com.swarup.e_restaurants.repository.CountryRepository;
import com.swarup.e_restaurants.repository.LocationRepository;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService{

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public ResponseEntity<?> findAllCountry() {
      List<Country> all = countryRepository.findAll();
      if (all.isEmpty()) {
        return ResponseHandler.generateResponse("No Country Prsent",HttpStatus.OK, null);
      } else {
        return ResponseHandler.generateResponse("Successfully country fetch",HttpStatus.OK, all);
      }
    }

    @Override
    public ResponseEntity<?> addCity(City city) {
      city.setStatus(true);
      cityRepository.save(city);
      return ResponseHandler.generateResponse("Successfully Saved the City",HttpStatus.OK, null);

    }

    @Override
    public ResponseEntity<?> fetchAllCity() {
    List<City> all = cityRepository.findAll();
    return ResponseHandler.generateResponse("Successfully Fetch List Of City",HttpStatus.OK, all);
    }

    @Override
    public ResponseEntity<?> findCityById(int id) {
      Optional<City> byId = cityRepository.findById(id);
      if (byId.isPresent()) {
      return ResponseHandler.generateResponse("Successfully Fetch Details",HttpStatus.OK, byId);
      } else {
        return ResponseHandler.generateResponse("Opps.. Something went wrong",HttpStatus.BAD_REQUEST, null);   
      }
    }

    @Override
    public ResponseEntity<?> addLocation(LocationMaster location) {
      location.setStatus(true);
      locationRepository.save(location);
      return ResponseHandler.generateResponse("Successfully Saved the City",HttpStatus.OK, null);
    }

    @Override
    public ResponseEntity<?> fetchAllLocation() {
      List<LocationMaster> all = locationRepository.findAll();
      for (LocationMaster location : all) {
        location.setCity(cityRepository.findById(location.getCityId()).get().getCityName());
      }
      return ResponseHandler.generateResponse("Successfully Fetch List Of City",HttpStatus.OK, all);   
     }

    @Override
    public ResponseEntity<?> findLocationById(int id) {
      Optional<LocationMaster> byId = locationRepository.findById(id);
      if (byId.isPresent()) {
      return ResponseHandler.generateResponse("Successfully Fetch Details",HttpStatus.OK, byId);
      } else {
       return ResponseHandler.generateResponse("Opps.. Something went wrong",HttpStatus.BAD_REQUEST, null);
      }
    }

    @Override
    public ResponseEntity<?> getLocationFindByCity(int cityId) {
     List<LocationMaster> allByCityId = locationRepository.findAllByCityId(cityId);
     return ResponseHandler.generateResponse("Successfully Fetch List Of City",HttpStatus.OK, allByCityId);   
    }
    
}
