package com.example.logistic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.Set;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @OneToMany
    private Set<Product> product;
    @ManyToOne
    @JsonIgnore
    private Customer customer;
    public Category(String name) {
        this.name = name;
    }

    public Category() {

    }
}
