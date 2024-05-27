package com.example.logistic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderCode;
    private Date orderDate;
    private String from_name;
    private String from_phone;
    private int weight;
    @ManyToOne
    private StatusOrder status;
    @ManyToOne
    private Warehouse warehouse;
    @ManyToOne
    @JsonIgnore
    private Customer customers;
    @OneToOne(cascade = CascadeType.ALL)
    private ReceiverInfo receiverInfo;
    @OneToOne(cascade = CascadeType.ALL)
    private Invoice invoice;
    private String note;
    private int payment_type_id;
    private String required_note;
    private int cod;
    private int total_price_product;
    private boolean warranty;
    private int transportFee;
    private int warrantyFee;
    private boolean payment_status;
    private String txnRef;
    public OrderProduct(Date orderDate, StatusOrder status, String note, int cod, boolean warranty, int payment_type_id,
                        String required_note, int weight, int total, boolean payment_status, String txnRef, int transportFee, int warrantyFee) {
        this.orderDate = orderDate;
        this.status = status;
        this.note = note;
        this.payment_type_id = payment_type_id;
        this.required_note = required_note;
        this.cod = cod;
        this.warranty = warranty;
        this.weight = weight;
        this.total_price_product = total;
        this.payment_status = payment_status;
        this.txnRef = txnRef;
        this.transportFee = transportFee;
        this. warrantyFee = warrantyFee;
    }
}
