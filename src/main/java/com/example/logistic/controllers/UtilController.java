package com.example.logistic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.*;
@Controller
@CrossOrigin
@RequestMapping("/api")
public class UtilController {
    @GetMapping("/setting/listAll")
    public @ResponseBody
    Object getBeers() {
        Resource resource = new ClassPathResource("/static/data.json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/currency/listAll")
    public @ResponseBody
    Object getBeers2(@RequestParam boolean enabled) {
        Resource resource = new ClassPathResource("/static/data2.json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
