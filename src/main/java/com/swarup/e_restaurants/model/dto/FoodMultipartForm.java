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
public class FoodMultipartForm {
    private int id;
    private int restId;
    private int typeId;
    private String name;
    private float price;
    private MultipartFile images;
}
