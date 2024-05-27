package com.example.logistic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String barcode;
    @NotNull
    private String name;
    private String brand;
    private String description;
    @NotNull
    private int price;
    private int importPrice;
    private int quantity;
    private String unit;
    @ManyToOne
    private Category category;
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("id")
    private Set<ImageData> images;
    @OneToOne(cascade = CascadeType.ALL)
    private ProductDetail productDetail;
    @ManyToOne
    @JsonIgnore
    private Customer customer;
    public Product(String name, String description, int price, int importPrice, int quantity, String unit, Category category, Set<ImageData> images, ProductDetail productDetail) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.importPrice = importPrice;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.images = images;
        this.productDetail = productDetail;
    }

    public Product(String name, String description, int price, int importPrice, int quantity, String unit, Set<ImageData> images, ProductDetail productDetail) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.importPrice = importPrice;
        this.quantity = quantity;
        this.unit = unit;
        this.images = images;
        this.productDetail = productDetail;
    }
}
