package com.example.logistic.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReceiverInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String city;
    private String district;
    private String ward;
    private String phoneNumber;

    public ReceiverInfo(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public ReceiverInfo(String name, String address, String city, String district, String ward, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.phoneNumber = phoneNumber;
    }
}
