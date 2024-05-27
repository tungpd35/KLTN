package com.example.logistic.dto;

import com.example.logistic.entities.StatusOrder;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderDto {
    private int status;
    private Long warehouse;
    private String address;
    private String city;
    private String district;
    private String ward;
    private String fullName;
    private String phoneNumber;
    private List<InvoiceDetailDto> invoiceDetailDtoList;
    private int cod;
    private String note;
    private int payment_type_id;
    private String required_note;
    private boolean warranty;
    private int weight;
    private int total_price_product;
    private boolean payment_status;
    private int transportFee;
    private int warrantyFee;
    private String txnRef;

    public boolean getWarranty() {
        return this.warranty;
    }
}
