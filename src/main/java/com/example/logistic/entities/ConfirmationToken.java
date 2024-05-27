package com.example.logistic.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String token;

    public ConfirmationToken(String name,String email, String password, String token, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
        this.phone = phone;
    }

    public ConfirmationToken() {

    }

}
