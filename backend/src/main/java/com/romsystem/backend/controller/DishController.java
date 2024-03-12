package com.romsystem.backend.controller;

import com.romsystem.backend.dto.DishRequestDTO;
import com.romsystem.backend.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dish")
public class DishController {

    private final DishService dishService;

    @PostMapping("/addDish")
    public ResponseEntity<?> addDish(@RequestBody DishRequestDTO dishRequestDTO) {
        return dishService.addDish(dishRequestDTO);
    }

    @DeleteMapping("/removeDish/{dishId}")
    public ResponseEntity<?> removeDish(@PathVariable Long dishId) {
        return dishService.removeDish(dishId);
    }

    @PutMapping("/updateDish/{dishId}")
    public ResponseEntity<?> updateDish(@PathVariable Long dishId, @RequestBody DishRequestDTO dishRequestDTO) {
        return dishService.updateDish(dishId, dishRequestDTO);
    }

    @PutMapping("/addInMenu/{dishId}")
    public ResponseEntity<?> addInMenu(@PathVariable Long dishId) {
        return dishService.updateDishStatus(dishId, true);
    }

    @PutMapping("/removeFromMenu/{dishId}")
    public ResponseEntity<?> removeFromMenu(@PathVariable Long dishId) {
        return dishService.updateDishStatus(dishId, false);
    }
}
