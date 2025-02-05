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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.swarup.e_restaurants.model.FoodCategories;
import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.dto.FoodMultipartForm;
import com.swarup.e_restaurants.repository.FoodCategoriesRepository;
import com.swarup.e_restaurants.repository.FoodTypeRepository;
import com.swarup.e_restaurants.repository.RestrurentRepository;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.FoodItemService;
@Service
public class FoodItemServiceImpl implements FoodItemService{

    @Autowired
    private FoodCategoriesRepository foodCategoriesRepository;

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private RestrurentRepository restrurentRepository;

    @Value("${file.upload-dir}")
    private static String UPLOAD_DIR;

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @Override
    public ResponseEntity<?> add(FoodMultipartForm multipartForm) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
            if (byEmail.isPresent()) {
                FoodCategories foods = new FoodCategories();
                foods.setStatus(true);
                foods.setRestId(byEmail.get().getId());
                foods.setName(multipartForm.getName());
                foods.setPrice(multipartForm.getPrice());
                foods.setTypeId(multipartForm.getTypeId());
              
                UUID uuid = UUID.randomUUID();

        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartFile multipartFile = multipartForm.getImages();
        String random = uuid.toString();
        String name = multipartFile.getOriginalFilename();

        System.out.println(name);
        String[] part = name.split("\\.");
        String extension = part[part.length - 1];
        // System.out.println(extension);
        String filename = random + "." + extension;

        if (multipartFile.isEmpty()) {
            return new ResponseEntity<String>("file not found", HttpStatus.BAD_REQUEST);
        } else {
            String uploadFilePath = uploadDirectory + "/" + filename;
          try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
            foods.setImages(filename);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }




                foodCategoriesRepository.save(foods);
                
                return ResponseHandler.generateResponse("Successfully created Restrurent", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse("Alre", HttpStatus.BAD_REQUEST, null);

            }

    }

    @Override
    public ResponseEntity<?> edit(FoodMultipartForm multipartForm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'edit'");
    }

    @Override
    public ResponseEntity<?> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        Optional<Restrurent> resturent = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
        if (resturent.isPresent()) {
        List<FoodCategories> all = foodCategoriesRepository.findAllByRestId(resturent.get().getId());
        if (all.isEmpty()) {
            return ResponseHandler.generateResponse("No vaild present...", HttpStatus.OK, null);
        } else {
           
            for (FoodCategories foodCategories : all) {
                foodCategories.setResturent(restrurentRepository.findById(foodCategories.getRestId()).get().getName());
                foodCategories.setFoodType(foodTypeRepository.findById(foodCategories.getTypeId()).get().getTypeDesc());
            }
            return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, all);

        }
    }else{
        return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, null);

    }
    }

    @Override
    public ResponseEntity<?> findAllActiveList() {
               List<FoodCategories> all = foodCategoriesRepository.findAll().stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        if (all.isEmpty()) {
            return ResponseHandler.generateResponse("No vaild present...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, all);
        }
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<FoodCategories> byId = foodCategoriesRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("No vaild present...", HttpStatus.BAD_REQUEST, null);

        }
    }

    @Override
    public ResponseEntity<?> active(Integer id) {
        Optional<FoodCategories> byId = foodCategoriesRepository.findById(id);
        if (!byId.get().isStatus()) {
            FoodCategories food = byId.get();
            food.setStatus(true);
            foodCategoriesRepository.save(food);
            return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Alreay Deactive...", HttpStatus.BAD_REQUEST, null);

        }
    }

    @Override
    public ResponseEntity<?> deActive(Integer id) {
           Optional<FoodCategories> byId = foodCategoriesRepository.findById(id);
        if (byId.get().isStatus()) {
            FoodCategories food = byId.get();
            food.setStatus(false);
            foodCategoriesRepository.save(food);
            return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Alreay Deactive...", HttpStatus.BAD_REQUEST, null);

        }
    }
    
}
