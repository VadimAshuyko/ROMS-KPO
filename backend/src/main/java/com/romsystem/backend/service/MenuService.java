package com.romsystem.backend.service;


import com.romsystem.backend.dto.DishRequestDTO;
import com.romsystem.backend.dto.DishResponseDTO;
import com.romsystem.backend.dto.ErrorResponseDTO;
import com.romsystem.backend.dto.MenuResponseDTO;
import com.romsystem.backend.entity.Dish;
import com.romsystem.backend.repository.DishRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuService {

    private final DishRepository dishRepository;

    public ResponseEntity<MenuResponseDTO> getMenu() {
        List<Dish> menu = dishRepository.findAllByIsInMenu(true);
        return new ResponseEntity<>(new MenuResponseDTO(menu), HttpStatus.OK);
    }

    public ResponseEntity<?> getDish(Long dishId) {
        Optional<Dish> dishCandidate = dishRepository.findById(dishId);
        if(dishCandidate.isPresent()){
            Dish dish = dishCandidate.get();
            return new ResponseEntity<>(new DishResponseDTO(
                    dish.getId(),
                    dish.getName(),
                    dish.getDescription(),
                    dish.getPrice(),
                    dish.getAmount(),
                    dish.getPreparationTime(),
                    dish.isInMenu()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorResponseDTO("Такого блюда нет"), HttpStatus.NOT_FOUND);
        }
    }
}
