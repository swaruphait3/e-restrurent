package com.swarup.e_restaurants.serviceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.model.dto.RestaurantMaltipartForm;
import com.swarup.e_restaurants.repository.CityRepository;
import com.swarup.e_restaurants.repository.LocationRepository;
import com.swarup.e_restaurants.repository.RestrurentRepository;
import com.swarup.e_restaurants.repository.UserRepositiry;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.RestrurentService;

@Service
public class RestrurentServiceImpl implements RestrurentService {

  @Autowired
  private RestrurentRepository restrurentRepository;

  @Autowired
  private UserRepositiry userRepositiry;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${file.upload-dir}")
  private static String UPLOAD_DIR;

  public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

  @Override
  public ResponseEntity<?> add(Restrurent restrurent) {
    boolean existsByNameAndEmail = restrurentRepository.existsByNameAndEmail(restrurent.getName(),
        restrurent.getEmail());
    if (!existsByNameAndEmail) {
      restrurent.setStatus(true);
      restrurentRepository.save(restrurent);
      boolean existsByEmail = userRepositiry.existsByEmail(restrurent.getEmail());
      if (!existsByEmail) {
        User user = new User();
        user.setName(restrurent.getName());
        user.setEmail(restrurent.getEmail());
        user.setPassword(passwordEncoder.encode(restrurent.getPassword()));
        user.setMobile(restrurent.getContact());
        user.setEnabled(true);
        user.setRole("RESTAURENT");
        userRepositiry.save(user);
      } else {
        Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restrurent.getEmail());
        User user = byEmailOrMobile.get();
        user.setRole("RESTAURENT");
        userRepositiry.save(user);
      }
      return ResponseHandler.generateResponse("Successfully created Restrurent", HttpStatus.OK, null);

    } else {
      return ResponseHandler.generateResponse("Shop Alreay Present...", HttpStatus.BAD_REQUEST, null);
    }
  }

  @Override
  public ResponseEntity<?> edit(Restrurent restrurent) {
    restrurentRepository.save(restrurent);
    return ResponseHandler.generateResponse("Successfully Updated Restrurent", HttpStatus.OK, null);

  };

  @Override
  public ResponseEntity<?> findAll() {
    List<Restrurent> all = restrurentRepository.findAll();
    if (all.isEmpty()) {
      return ResponseHandler.generateResponse("No vaild present...", HttpStatus.OK, null);
    } else {
      for (Restrurent restrurent : all) {
        restrurent.setCity(cityRepository.findById(restrurent.getCityId()).get().getCityName());
        restrurent.setLocation(locationRepository.findById(restrurent.getLocId()).get().getLocation());
      }
      return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, all);

    }
  }

  @Override
  public ResponseEntity<?> findAllActiveList() {
    List<Restrurent> all = restrurentRepository.findAll().stream().filter(t -> t.isStatus())
        .collect(Collectors.toList());
    if (all.isEmpty()) {
      return ResponseHandler.generateResponse("No vaild present...", HttpStatus.OK, null);
    } else {
      return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, all);
    }

  }

  @Override
  public ResponseEntity<?> findById(Integer id) {
    Optional<Restrurent> byId = restrurentRepository.findById(id);
    if (byId.isPresent()) {
      return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, byId);
    } else {
      return ResponseHandler.generateResponse("No vaild present...", HttpStatus.BAD_REQUEST, null);

    }
  }

  @Override
  public ResponseEntity<?> active(Integer id) {
    Optional<Restrurent> byId = restrurentRepository.findById(id);
    if (!byId.get().isStatus()) {
      Restrurent restrurent = byId.get();
      restrurent.setStatus(true);
      restrurentRepository.save(restrurent);
      Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restrurent.getEmail());
      byEmailOrMobile.get().setEnabled(true);
      userRepositiry.save(byEmailOrMobile.get());
      return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, null);
    } else {
      return ResponseHandler.generateResponse("Alreay Deactive...", HttpStatus.BAD_REQUEST, null);

    }
  }

  @Override
  public ResponseEntity<?> deActive(Integer id) {
    Optional<Restrurent> byId = restrurentRepository.findById(id);
    if (byId.get().isStatus()) {
      Restrurent restrurent = byId.get();
      restrurent.setStatus(false);
      restrurentRepository.save(restrurent);
      Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restrurent.getEmail());
      byEmailOrMobile.get().setEnabled(false);
      userRepositiry.save(byEmailOrMobile.get());
      return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, null);
    } else {
      return ResponseHandler.generateResponse("Alreay Deactive...", HttpStatus.BAD_REQUEST, null);

    }
  }

  @Override
  public ResponseEntity<?> addResaturant(RestaurantMaltipartForm restaurantMaltipartForm) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

    boolean existsByNameAndEmail = restrurentRepository.existsByNameAndEmail(restaurantMaltipartForm.getName(),
        restaurantMaltipartForm.getEmail());
    if (!existsByNameAndEmail) {
      Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
      if (byEmail.isPresent()) {
        Restrurent restrurent = new Restrurent();
        restrurent.setStatus(true);
        restrurent.setLocId(restaurantMaltipartForm.getLocId());
        restrurent.setCityId(restaurantMaltipartForm.getCityId());
        restrurent.setName(restaurantMaltipartForm.getName());
        restrurent.setEmail(restaurantMaltipartForm.getEmail());
        restrurent.setContact(restaurantMaltipartForm.getContact());
        restrurent.setAddress(restaurantMaltipartForm.getAddress());
        restrurent.setSpecality(restaurantMaltipartForm.getSpecality());
        UUID uuid = UUID.randomUUID();

        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
          uploadDir.mkdirs();
        }

        MultipartFile multipartFile = restaurantMaltipartForm.getImages();
        String random = uuid.toString();
        String name = multipartFile.getOriginalFilename();

        System.out.println(name);
        String[] part = name.split("\\.");
        String extension = part[part.length - 1];
        System.out.println(extension);
        String filename = random + "." + extension;

        if (multipartFile.isEmpty()) {
          return new ResponseEntity<String>("file not found", HttpStatus.BAD_REQUEST);
        } else {
          String uploadFilePath = uploadDirectory + "/" + filename;
          try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
            restrurent.setImage(filename);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        restrurentRepository.save(restrurent);
        boolean existsByEmail = userRepositiry.existsByEmail(restaurantMaltipartForm.getEmail());
        if (!existsByEmail) {
          User user = new User();
          user.setName(byEmail.get().getName());
          user.setEmail(restaurantMaltipartForm.getEmail());
          user.setPassword(passwordEncoder.encode(restaurantMaltipartForm.getPassword()));
          user.setMobile(restaurantMaltipartForm.getContact());
          user.setEnabled(true);
          user.setRole("RESTAURENT");
          userRepositiry.save(user);
        } else {
          Optional<User> byEmailOrMobile = userRepositiry.findByEmail(restaurantMaltipartForm.getEmail());
          User user = byEmailOrMobile.get();
          user.setName(byEmail.get().getName());
          user.setRole("RESTAURENT");
          userRepositiry.save(user);
        }
        return ResponseHandler.generateResponse("Successfully created Restrurent", HttpStatus.OK, null);
      } else {
        return ResponseHandler.generateResponse("Alre", HttpStatus.BAD_REQUEST, null);

      }

    } else {
      return ResponseHandler.generateResponse("Shop Alreay Present...", HttpStatus.BAD_REQUEST, null);
    }
  }

}
