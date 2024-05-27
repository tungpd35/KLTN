package com.example.logistic.controllers;

import com.example.logistic.dto.ProductDto;
import com.example.logistic.entities.Product;
import com.example.logistic.entities.User;
import com.example.logistic.models.ProductPage;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.ProductRepository;
import com.example.logistic.services.ProductRedisService;
import com.example.logistic.services.ProductService;
import com.example.logistic.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRedisService productRedisService;
    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestHeader("Authorization") String token, @RequestBody ProductDto productDto) {
        User user = userService.getUserFromToken(token);
        Product product = productService.mapToProduct(productDto);
        user.getCustomer().getProducts().add(product);
        product.setCustomer(user.getCustomer());
        Product newProduct = productRepository.save(product);
        productRedisService.clear();
        return ResponseEntity.ok(new Response(200,true,newProduct));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token,@PathVariable Long id, @RequestBody ProductDto productDto) {
        User user = userService.getUserFromToken(token);
        Product product = productService.mapToProduct(productDto);
        Product oldProduct = productRepository.getProductById(id);
        if(!oldProduct.getCustomer().getId().equals(user.getCustomer().getId())) {
            ResponseEntity.status(403).body("You don’t have permission to access");
        }
        oldProduct.setProductDetail(product.getProductDetail());
        oldProduct.setBrand(product.getBrand());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setImages(product.getImages());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setImportPrice(product.getImportPrice());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setQuantity(product.getQuantity());
        oldProduct.setUnit(product.getUnit());
        oldProduct.setName(product.getName());
        Product newProduct = productRepository.save(oldProduct);
        productRedisService.clear();
        return ResponseEntity.ok(new Response(200,true,newProduct));
    }
    @GetMapping("/list")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token,
                                @RequestParam int page, @RequestParam int page_size,
                                @RequestParam String keyword) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page,page_size);
        User user = userService.getUserFromToken(token);
        ProductPage products = productRedisService.getAllProducts(user.getCustomer().getId(), keyword, pageRequest);
        if(products == null) {
            Page<Product> productPage = productRepository.getProducts(keyword, user.getCustomer().getId(),pageRequest);
            ProductPage productPage1 = new ProductPage(productPage.getContent(),productPage.getTotalPages(),productPage.getTotalElements(),
                    productPage.getNumber());
            productRedisService.saveAllProducts(productPage1, user.getCustomer().getId(), keyword, pageRequest);
            return ResponseEntity.ok(productPage1);
        }
        return ResponseEntity.ok(products);
    }
    @GetMapping("/detail")
    public ResponseEntity<?> getDetail(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        User user = userService.getUserFromToken(token);
        Product product = productRepository.getProductById(id);
        if (!product.getCustomer().getId().equals(user.getCustomer().getId())) return ResponseEntity.status(403).body("You don’t have permission to access");
        return ResponseEntity.ok().body(product);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        User user = userService.getUserFromToken(token);
        Product product = productRepository.getProductById(id);
        productRepository.delete(product);
        productRedisService.clear();
        return ResponseEntity.ok(new Response(200,true,""));
    }
}
