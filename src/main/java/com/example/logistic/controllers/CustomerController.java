package com.example.logistic.controllers;

import com.example.logistic.entities.User;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.UserRepository;
import com.example.logistic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customer")
@CrossOrigin
public class CustomerController {
    @Autowired
    private UserService userService;
    @GetMapping("/getAvatar")
    public ResponseEntity<?> getAvatar(@RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok(new Response(200,true,user.getCustomer().getAvatar()));
    }
}
