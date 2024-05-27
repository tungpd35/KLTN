package com.example.logistic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Role role;
    private boolean isEnabled;
    private String avatar;
    public User(String email, String password, Role role, Customer customer,String avatar) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.customer = customer;
        this.isEnabled = true;
        this.avatar = avatar;
    }

    public User(String email, String password, Role role, Staff staff,String avatar) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.staff = staff;
        this.isEnabled = true;
        this.avatar = avatar;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;
    @ManyToOne(cascade = CascadeType.ALL)
    private Staff staff;
    @OneToOne
    private PasswordResetToken passwordResetToken;
}
