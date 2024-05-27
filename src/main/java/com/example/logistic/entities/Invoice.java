package com.example.logistic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.Order;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date invoiceDate;
    private int totalPrice;
    @OneToOne
    @JsonIgnore
    private OrderProduct orderProduct;
    @ManyToOne
    @JsonIgnore
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL)
    private List<InvoiceDetail> invoiceDetailList;
}
