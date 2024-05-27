package com.example.logistic.controllers;

import com.example.logistic.entities.Warehouse;
import com.example.logistic.repositories.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/warehouse")
@CrossOrigin
public class WarehouseController {
    @Autowired
    private WareHouseRepository wareHouseRepository;
    @GetMapping("/list")
    public List<Warehouse> getAll() {
        return wareHouseRepository.findAll();
    }
}
