package com.swarup.e_restaurants.model.dto;

import javax.persistence.Transient;

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
public class RestaurantMaltipartForm {
    private int id;
    private int locId;
    private int cityId;
    private String name;
    private String address;
    private String contact;
    private String email;
    private String specality;
    private String profile;
    private MultipartFile images;
    @Transient
    private String password;

}
