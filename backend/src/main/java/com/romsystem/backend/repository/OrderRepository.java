package com.romsystem.backend.repository;

import com.romsystem.backend.entity.Order;
import com.romsystem.backend.enums.OrderStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    void deleteById(@NonNull Long id);

    List<Order> findAllByOrderStatus(@NonNull OrderStatus status);

    List<Order> findAll();
}

