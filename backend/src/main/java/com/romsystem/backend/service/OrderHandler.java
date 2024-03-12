package com.romsystem.backend.service;


import com.romsystem.backend.dto.OrderDTO;
import com.romsystem.backend.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class OrderHandler {
    private final OrderService orderService;

    @Autowired
    @Qualifier("orderProcessExecutor")
    private ExecutorService orderProcessExecutor;

    @Autowired
    @Qualifier("addPositionExecutor")
    private ExecutorService addPositionExecutor;

    @Autowired
    @Qualifier("cancelOrderExecutor")
    private ExecutorService cancelOrderExecutor;

    public CompletableFuture<Order> updateOrder(Long orderId, OrderDTO dto) {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return CompletableFuture.supplyAsync(() -> orderService.upload(orderId, name, dto), addPositionExecutor);
    }

    public CompletableFuture<Order> handleOrder(OrderDTO dto) {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return orderService.create(dto, name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, orderProcessExecutor).thenApply(order -> {
            try {
                return orderService.status(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Order> payOrder(Long orderId) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return CompletableFuture.supplyAsync(() -> orderService.payOrder(orderId, username));
    }

    public CompletableFuture<Order> cancelOrder(Long orderId) {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return CompletableFuture.supplyAsync(() -> orderService.cancel(orderId, name), cancelOrderExecutor);
    }
}
