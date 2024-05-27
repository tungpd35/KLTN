package com.example.logistic.services;

import com.example.logistic.dto.ProductDto;
import com.example.logistic.entities.Category;
import com.example.logistic.entities.Product;
import com.example.logistic.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private CategoryRepository categoryRepository;
    public Product mapToProduct(ProductDto productDto) {
        Category category = categoryRepository.findCategoriesByName(productDto.getCategory());
        return new Product(productDto.getName(), productDto.getDescription(),productDto.getPrice(),productDto.getImportPrice(),productDto.getStock(),productDto.getUnit(),category,productDto.getImages(),productDto.getProductDetail());
    }
}
