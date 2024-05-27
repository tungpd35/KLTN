package com.example.logistic.dto;

import com.example.logistic.entities.ImageData;
import com.example.logistic.entities.Product;
import com.example.logistic.entities.ProductDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductDto {
    private String name;
    private String unit;
    private String category;
    private int stock;
    private String description;
    private int price;
    private int importPrice;
    private String wareHouseType;
    private ProductDetail productDetail;
    private Set<ImageData> images;
}
