package com.romsystem.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Dish dish;
    private int amount;
}
