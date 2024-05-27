package com.example.logistic.entities;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String from;
    private String text;
    private String time;
}
