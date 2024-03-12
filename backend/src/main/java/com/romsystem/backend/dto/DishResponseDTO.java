package com.romsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DishResponseDTO {
        private Long id;
        private String name;
        private String description;
        private double price;
        private int amount;
        private long preparationTime;
        private boolean isInMenu;
}
