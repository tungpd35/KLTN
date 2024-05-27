package com.example.logistic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String avatar;
    private String address;
    private String gender;
    private String phoneNumber;
    @OneToMany
    private Set<Product> products;
    @OneToMany
    private Set<User> users;
    @OneToMany
    private List<Category> categories;
    @OneToMany
    private List<OrderProduct> orderProducts;
    @OneToMany
    private List<Receiver> receivers;
}
