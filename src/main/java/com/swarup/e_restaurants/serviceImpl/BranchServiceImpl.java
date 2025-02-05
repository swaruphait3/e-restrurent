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

import com.swarup.e_restaurants.model.Branch;
import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.Restrurent;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.model.dto.BranchMaltipartForm;
import com.swarup.e_restaurants.repository.BranchRepository;
import com.swarup.e_restaurants.repository.CityRepository;
import com.swarup.e_restaurants.repository.LocationRepository;
import com.swarup.e_restaurants.repository.RestrurentRepository;
import com.swarup.e_restaurants.repository.UserRepositiry;
import com.swarup.e_restaurants.response.ResponseHandler;
import com.swarup.e_restaurants.service.BranchService;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RestrurentRepository restrurentRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private static String UPLOAD_DIR;

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @Override
    public ResponseEntity<?> add(Branch branch) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        boolean existsByNameAndEmail = branchRepository.existsByBranchNameAndBranchEmail(branch.getBranchName(),
                branch.getBranchEmail());
        if (!existsByNameAndEmail) {
            Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
            if (byEmail.isPresent()) {
                branch.setStatus(true);
                branch.setRestId(byEmail.get().getId());
                branchRepository.save(branch);
                boolean existsByEmail = userRepositiry.existsByEmail(branch.getBranchEmail());
                if (!existsByEmail) {
                    User user = new User();
                    user.setName(byEmail.get().getName()+" ("+branch.getBranchName()+")");
                    user.setEmail(branch.getBranchEmail());
                    user.setPassword(passwordEncoder.encode(branch.getPassword()));
                    user.setMobile(branch.getBranchContact());
                    user.setEnabled(true);
                    user.setRole("BRANCH");
                    userRepositiry.save(user);
                } else {
                    Optional<User> byEmailOrMobile = userRepositiry.findByEmail(branch.getBranchEmail());
                    User user = byEmailOrMobile.get();
                    user.setName(byEmail.get().getName()+" ("+branch.getBranchName()+")");
                    user.setRole("BRANCH");
                    userRepositiry.save(user);
                }
                return ResponseHandler.generateResponse("Successfully created Restrurent", HttpStatus.OK, null);
            } else {

            }

        } else {
            return ResponseHandler.generateResponse("Shop Alreay Present...", HttpStatus.BAD_REQUEST, null);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> edit(Branch branch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'edit'");
    }

    @Override
    public ResponseEntity<?> findAll() {
        List<Branch> all = branchRepository.findAll();
        if (all.isEmpty()) {
            return ResponseHandler.generateResponse("No vaild present...", HttpStatus.OK, null);
        } else {
            for (Branch branch : all) {
                branch.setCity(cityRepository.findById(branch.getCityId()).get().getCityName());
                branch.setLocation(locationRepository.findById(branch.getLocationId()).get().getLocation());
                branch.setRestrurent(restrurentRepository.findById(branch.getRestId()).get().getName());
            }
            return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, all);

        }
    }

    @Override
    public ResponseEntity<?> findAllActiveList() {
        List<Branch> all = branchRepository.findAll().stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        if (all.isEmpty()) {
            return ResponseHandler.generateResponse("No vaild present...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Successful fetch Data...", HttpStatus.OK, all);
        }
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<Branch> byId = branchRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("No vaild present...", HttpStatus.BAD_REQUEST, null);

        }
    }

    @Override
    public ResponseEntity<?> active(Integer id) {
        Optional<Branch> byId = branchRepository.findById(id);
        if (!byId.get().isStatus()) {
            Branch branch = byId.get();
            branch.setStatus(false);
            branchRepository.save(branch);
            Optional<User> byEmailOrMobile = userRepositiry.findByEmail(branch.getBranchEmail());
            byEmailOrMobile.get().setEnabled(false);
            userRepositiry.save(byEmailOrMobile.get());
            return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Alreay Deactive...", HttpStatus.BAD_REQUEST, null);

        }
    }

    @Override
    public ResponseEntity<?> deActive(Integer id) {
        Optional<Branch> byId = branchRepository.findById(id);
        if (byId.get().isStatus()) {
            Branch branch = byId.get();
            branch.setStatus(true);
            branchRepository.save(branch);
            Optional<User> byEmailOrMobile = userRepositiry.findByEmail(branch.getBranchEmail());
            byEmailOrMobile.get().setEnabled(true);
            userRepositiry.save(byEmailOrMobile.get());
            return ResponseHandler.generateResponse("Successful fetch the details...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Alreay Deactive...", HttpStatus.BAD_REQUEST, null);

        }
    }

    @Override
    public ResponseEntity<?> addBranch(BranchMaltipartForm branchForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        boolean existsByNameAndEmail = branchRepository.existsByBranchNameAndBranchEmail(branchForm.getBranchName(),
        branchForm.getBranchEmail());
        if (!existsByNameAndEmail) {
            Optional<Restrurent> byEmail = restrurentRepository.findByEmail(userDetails.getUser().getEmail());
            if (byEmail.isPresent()) {
                Branch branch = new Branch();
                branch.setStatus(true);
                branch.setRestId(byEmail.get().getId());
                branch.setBranchName(branchForm.getBranchName());
                branch.setBranchEmail(branchForm.getBranchEmail());
                branch.setBranchContact(branchForm.getBranchContact());
                branch.setCityId(branchForm.getCityId());
                branch.setLocationId(branchForm.getLocationId());
                branch.setLocAddress(branchForm.getLocAddress());
                UUID uuid = UUID.randomUUID();

        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartFile multipartFile = branchForm.getImages();
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
            branch.setImage(filename);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }




                branchRepository.save(branch);
                boolean existsByEmail = userRepositiry.existsByEmail(branchForm.getBranchEmail());
                if (!existsByEmail) {
                    User user = new User();
                    user.setName(byEmail.get().getName()+" ("+branchForm.getBranchName()+")");
                    user.setEmail(branchForm.getBranchEmail());
                    user.setPassword(passwordEncoder.encode(branchForm.getPassword()));
                    user.setMobile(branchForm.getBranchContact());
                    user.setEnabled(true);
                    user.setRole("BRANCH");
                    userRepositiry.save(user);
                } else {
                    Optional<User> byEmailOrMobile = userRepositiry.findByEmail(branchForm.getBranchEmail());
                    User user = byEmailOrMobile.get();
                    user.setName(byEmail.get().getName()+" ("+branch.getBranchName()+")");
                    user.setRole("BRANCH");
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
