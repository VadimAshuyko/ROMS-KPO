package com.romsystem.backend.service;


import com.romsystem.backend.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final OrderService orderService;

    public BigDecimal getRevenueAtAllTime() {
        var orders = orderService.getAllByStatus(OrderStatus.PAID);
        BigDecimal sum = BigDecimal.ZERO;
        for (var order : orders) {
            BigDecimal sumInOrder = BigDecimal.ZERO;
            for (var dishAmount : order.getPositions()) {
                var dish = dishAmount.getDish();
                var price = BigDecimal.valueOf(dish.getPrice()).multiply(BigDecimal.valueOf(dishAmount.getAmount())).setScale(2, RoundingMode.HALF_UP);
                sumInOrder = sumInOrder.add(price).setScale(2, RoundingMode.HALF_UP);
            }
            sum = sum.add(sumInOrder).setScale(2, RoundingMode.HALF_UP);
        }
        return sum;
    }
}
