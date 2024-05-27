package com.example.logistic.models;

import com.example.logistic.entities.Product;
import lombok.*;

import java.util.List;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPage {
    private List<Product> content;
    private int totalPages;
    private long totalElements;
    private int number;
}
