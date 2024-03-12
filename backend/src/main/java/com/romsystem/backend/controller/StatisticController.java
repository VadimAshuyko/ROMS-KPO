package com.romsystem.backend.controller;


import com.romsystem.backend.entity.Dish;
import com.romsystem.backend.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/popular")
    public ResponseEntity<List<Dish>> getMostPopularDishes() {
        return ResponseEntity.ok(statisticService.getMostPopularDishes());
    }

    @GetMapping("/amount")
    public ResponseEntity<Integer> getAmountOfOrders() {
        return ResponseEntity.ok(statisticService.getAmountOfOrders());
    }

    @GetMapping("/grade")
    public ResponseEntity<BigDecimal> getAverageGradeOfDishes() {
        return ResponseEntity.ok(statisticService.getAverageGradeOfDish());
    }
}
