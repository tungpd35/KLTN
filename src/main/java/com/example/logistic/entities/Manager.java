package com.example.logistic.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String phoneNumber;
    @OneToMany
    private List<Staff> staff;
}
