package com.example.logistic.controllers;

import com.example.logistic.entities.Product;
import com.example.logistic.entities.Receiver;
import com.example.logistic.entities.User;
import com.example.logistic.payload.ErrorResponse;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.ReceiverRepository;
import com.example.logistic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/receiver")
@CrossOrigin
public class ReceiverController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReceiverRepository receiverRepository;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody Receiver receiver) {
        User user = userService.getUserFromToken(token);
        if(receiverRepository.findByPhoneNumberAndCustomer(receiver.getPhoneNumber(), user.getCustomer()) != null) {
            return ResponseEntity.status(400).body(new ErrorResponse(null,400,"Khách hàng đã tồn tai"));
        } else {
            receiver.setCustomer(user.getCustomer());
            user.getCustomer().getReceivers().add(receiver);
            receiverRepository.save(receiver);
            return ResponseEntity.ok(new Response( 200,true,receiver));
        }
    }
    @GetMapping("/list")
    public Page<Receiver> getAll(@RequestHeader("Authorization") String token,
                                @RequestParam int page, @RequestParam int page_size,
                                @RequestParam String keyword) {
        PageRequest pageRequest = PageRequest.of(page,page_size);
        User user = userService.getUserFromToken(token);
        return receiverRepository.getReceivers(keyword,user.getCustomer().getId(),pageRequest);
    }
}
