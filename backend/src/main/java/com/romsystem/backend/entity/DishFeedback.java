package com.romsystem.backend.entity;

import com.romsystem.backend.enums.Grade;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DishFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Grade grade;
    private String comment;
    private Long authorId;
    private Long dishId;
}
