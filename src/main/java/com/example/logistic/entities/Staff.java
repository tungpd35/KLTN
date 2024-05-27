package com.example.logistic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Date birthday;
    private String gender;
    @ManyToOne
    private Warehouse warehouse;
    @OneToMany
    private Set<User> users;
}
