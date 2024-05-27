package com.example.logistic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String image;
    private Date createTime;
    @OneToOne
    private IDInfo id_info;
    private Long userId;
    private boolean unread;
}
