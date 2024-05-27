package com.example.logistic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDetailDto {
    private Long productId;
    private int price;
    private int quantity;
}
