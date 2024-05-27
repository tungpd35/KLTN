package com.example.logistic.services;

import com.example.logistic.entities.Product;
import com.example.logistic.models.ProductPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    void clear();
    ProductPage getAllProducts(Long id, String keyword, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllProducts(ProductPage products, Long id, String keyword, PageRequest pageRequest) throws  JsonProcessingException;
}
