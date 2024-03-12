package com.romsystem.backend.dto;


import com.romsystem.backend.entity.Dish;
import lombok.Data;

import java.util.List;

@Data
public class MenuResponseDTO {
    List<Dish> menu;
    public MenuResponseDTO(List<Dish> menu){
        this.menu = menu;
    }
}
