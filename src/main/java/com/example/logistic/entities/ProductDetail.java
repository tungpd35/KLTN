package com.example.logistic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private int weight;
    @NotNull
    private int height;
    @NotNull
    private int width;
    @NotNull
    private int length;
    private boolean fragile;
    private String wareHouseType;
    private String packInfo;
    private String strategy;
    @OneToOne
    private Product product;

}
