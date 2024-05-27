package com.example.logistic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Invoice invoice;
    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;
    private int quantity;
    private int unitPrice;

    public InvoiceDetail(Invoice invoice, Product product, int quantity, int unitPrice) {
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
