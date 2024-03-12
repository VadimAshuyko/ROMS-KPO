package com.romsystem.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "preparation_time", nullable = false)
    private long preparationTime;

    @Column(name = "is_in_menu", nullable = false)
    private boolean isInMenu;

    @Column(name = "amount", nullable = false)
    private int amount;
}
