package com.romsystem.backend.service;

import com.romsystem.backend.entity.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final DishFeedbackService dishFeedbackService;
    private  final DishService dishService;
    private  final OrderService orderService;

    public Integer getAmountOfOrders() {
        return orderService.getAllOrders().size();
    }

    public List<Dish> getMostPopularDishes() {
        var orders = orderService.getAllOrders();
        Map<Long, Integer> dishMap = new HashMap<>();
        for (var order : orders) {
            for (var dishAmount : order.getPositions()) {
                dishMap.put(
                        dishAmount.getDish().getId(),
                        dishMap.getOrDefault(dishAmount.getDish().getId(), 0)
                                + dishAmount.getAmount()
                );
            }
        }
        var maxValue = dishMap.values().stream()
                .max(Comparator.comparingInt(a -> a))
                .orElseThrow(() -> new IllegalStateException("(most popular dishes) error"));
        List<Long> dishIdList = new ArrayList<>();
        for (var dishAmount : dishMap.entrySet()) {
            if (dishAmount.getValue().equals(maxValue)) {
                dishIdList.add(dishAmount.getKey());
            }
        }
        return dishIdList.stream().map(dishService::get).collect(Collectors.toList());
    }

    public BigDecimal getAverageGradeOfDish() {
        var feedbacks = dishFeedbackService.getAll();
        var sumOfGrades = feedbacks.stream()
                .map(it -> new BigDecimal(it.getGrade().ordinal() + 1))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sumOfGrades.divide(new BigDecimal(feedbacks.size())).setScale(2, RoundingMode.HALF_UP);
    }

}
