package com.beeline.testproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Sales {
    @Id()
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private Long customerId;
    private String item;
    private Integer price;

    private LocalDateTime createdDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private LocalDateTime deletedDate;
}