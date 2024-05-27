package com.example.logistic.controllers;

import com.example.logistic.entities.Category;
import com.example.logistic.entities.User;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.CategoryRepository;
import com.example.logistic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token,@RequestBody Category category) {
        User user = userService.getUserFromToken(token);
        category.setCustomer(user.getCustomer());
        user.getCustomer().getCategories().add(category);
        Category newCate = categoryRepository.save(category);
        return ResponseEntity.ok(new Response(200,true,newCate));
    }
    @GetMapping("/list")
    public Page<Category> getAllBy(@RequestHeader("Authorization") String token, @RequestParam(required = false) String keyword, @RequestParam int page, @RequestParam int page_size) {
        PageRequest pageRequest = PageRequest.of(page,page_size);
        User user = userService.getUserFromToken(token);
        return categoryRepository.searchCategories(keyword,user.getCustomer().getId(),pageRequest);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok().body(new Response(200,true,categoryRepository.findAll()));
    }
}
