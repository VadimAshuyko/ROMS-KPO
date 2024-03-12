package com.romsystem.backend.service;

import com.romsystem.backend.dto.DishRequestDTO;
import com.romsystem.backend.dto.DishResponseDTO;
import com.romsystem.backend.dto.ErrorResponseDTO;
import com.romsystem.backend.entity.Dish;
import com.romsystem.backend.entity.Position;
import com.romsystem.backend.repository.DishRepository;
import com.romsystem.backend.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final PositionRepository positionRepository;

    public ResponseEntity<?> addDish(DishRequestDTO dishRequestDTO) {
        Dish dish = new Dish();
        dish.setName(dishRequestDTO.getName());
        dish.setDescription(dishRequestDTO.getDescription());
        dish.setPrice(dishRequestDTO.getPrice());
        dish.setAmount(dishRequestDTO.getAmount());
        dish.setPreparationTime(dishRequestDTO.getPreparationTime());
        dish.setInMenu(dishRequestDTO.isInMenu());
        dishRepository.save(dish);
            return new ResponseEntity<>(new DishResponseDTO(
                    dish.getId(),
                    dish.getName(),
                    dish.getDescription(),
                    dish.getPrice(),
                    dish.getAmount(),
                    dish.getPreparationTime(),
                    dish.isInMenu()), HttpStatus.OK);
    }

    public ResponseEntity<?> removeDish(Long dishId) {
        Optional<Dish> dishCandidate = dishRepository.findById(dishId);
        if(dishCandidate.isPresent()){
            dishRepository.deleteById(dishId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorResponseDTO("Такого блюда нет"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateDish(Long dishId, DishRequestDTO dishRequestDTO) {
        Optional<Dish> dishCandidate = dishRepository.findById(dishId);
        if(dishCandidate.isPresent()){
            Dish dish = dishCandidate.get();
            dish.setName(dishRequestDTO.getName());
            dish.setDescription(dishRequestDTO.getDescription());
            dish.setPrice(dishRequestDTO.getPrice());
            dish.setAmount(dishRequestDTO.getAmount());
            dish.setPreparationTime(dishRequestDTO.getPreparationTime());
            dish.setInMenu(dishRequestDTO.isInMenu());
            dishRepository.save(dish);
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

    public synchronized ResponseEntity<?> updateDishStatus(Long dishId, boolean isInMenu) {
        Optional<Dish> dishCandidate = dishRepository.findById(dishId);
        if(dishCandidate.isPresent()){
            Dish dish = dishCandidate.get();
            dish.setInMenu(isInMenu);
            dishRepository.save(dish);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorResponseDTO("Такого блюда нет"), HttpStatus.NOT_FOUND);
        }
    }


    public synchronized Dish get(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Dish not found"));
    }

    @Transactional
    public synchronized List<Position> getOrders(final Map<Long, Integer> dto) {
        List<Position> positionArrayList = new ArrayList<>();
        for (var dishOrder : dto.entrySet()) {
            var dish = dishRepository.findById(dishOrder.getKey()).orElseThrow(() -> new IllegalArgumentException("Dish not found"));
            if (!dish.isInMenu()) {
                throw new IllegalArgumentException("Dish is not in order");
            }
            if (dish.getAmount() < dishOrder.getValue()) {
                throw new IllegalStateException("Dish amount is not enough");
            }
            dish.setAmount(dish.getAmount() - dishOrder.getValue());
            if (dish.getAmount() == 0) {
                dish.setInMenu(false);
            }
            dishRepository.save(dish);

            var position = new Position();
            position.setDish(dish);
            position.setAmount(dishOrder.getValue());

            positionArrayList.add(positionRepository.save(position));
        }

        return positionArrayList;
    }
}
