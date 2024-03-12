package com.romsystem.backend.controller;

import com.romsystem.backend.dto.OrderDTO;
import com.romsystem.backend.entity.Order;
import com.romsystem.backend.service.OrderHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
private final OrderHandler orderHandler;

    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestBody OrderDTO orderDTO) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(orderHandler.handleOrder(orderDTO).get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody OrderDTO dto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(orderHandler.updateOrder(id, dto).get());
    }

    @PutMapping("/pay/{id}")
    public ResponseEntity<Order> pay(@PathVariable Long id) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(orderHandler.payOrder(id).get());
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Order> cancel(@PathVariable Long id) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(orderHandler.cancelOrder(id).get());
    }
}
