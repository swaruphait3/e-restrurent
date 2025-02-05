package com.swarup.e_restaurants.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BranchMaltipartForm {
    private int id;
    private int restId;
    private int cityId;
    private int locationId;
    private String locAddress;
    private String branchName;
    private String branchEmail;
    private String branchContact;
    private String password;
    private MultipartFile Images;
}
