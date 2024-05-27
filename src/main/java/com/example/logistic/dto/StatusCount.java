package com.example.logistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public interface StatusCount {
    Long getId();
    Integer getGroupId();
    String getName();
    Integer getCount();
}
