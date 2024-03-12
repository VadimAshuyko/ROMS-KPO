package com.romsystem.backend.service;

import com.romsystem.backend.dto.OrderDTO;
import com.romsystem.backend.entity.Order;
import com.romsystem.backend.enums.OrderStatus;
import com.romsystem.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final DishService dishService;

    @Transactional
    public Order create(OrderDTO dto, String email) {
        var client = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        var positionsList = dishService.getOrders(dto.getDishes());
        Order order = new Order();
        order.setClientId(client.getId());
        order.setPositions(positionsList);
        order.setDateTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CREATED);
        return save(order);
    }

    public OrderStatus getStatus(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found")).getOrderStatus();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    private synchronized Order save(Order order) {
        return orderRepository.save(order);
    }

    public synchronized Order get(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("Order %d not found", id)));
    }

    public Order status(Order order) throws InterruptedException {
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        save(order);
        var positions = order.getPositions();
        int begin = 0;
        while (begin != positions.size()) {
            var amount = positions.get(begin);
            var dish = amount.getDish();
            for (int i = 0; i < amount.getAmount(); i++) {
                Thread.sleep(dish.getPreparationTime());
                var updatedOrder = get(order.getId());
                if (updatedOrder.getOrderStatus() == OrderStatus.CANCELED) {
                    return updatedOrder;
                }
            }
            positions = get(order.getId()).getPositions();
            System.out.println(positions.size());
            ++begin;
        }
        order = get(order.getId());
        order.setOrderStatus(OrderStatus.COMPLETED);
        return save(order);
    }

    @Transactional
    public Order upload(Long orderId, String email, OrderDTO dto) {
        var orderToUpdate = get(orderId);
        var client = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Objects.equals(orderToUpdate.getClientId(), client.getId())) {
            throw new IllegalArgumentException("No rights to this order");
        }
        if (orderToUpdate.getOrderStatus().ordinal() > OrderStatus.IN_PROGRESS.ordinal()) {
            throw new IllegalStateException("Can not be updated");
        }
        var newDishAmountList = dishService.getOrders(dto.getDishes());
        var dishAmounts = orderToUpdate.getPositions();
        dishAmounts.addAll(newDishAmountList);
        return save(orderToUpdate);
    }

    @Transactional
    public Order cancel(Long id, String email) {
        var order = get(id);
        var client = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Objects.equals(order.getClientId(), client.getId())) {
            throw new IllegalArgumentException("No rights to this order");
        }
        if (order.getOrderStatus().ordinal() > OrderStatus.IN_PROGRESS.ordinal()) {
            throw new IllegalStateException("Can not be canceled");
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        return save(order);
    }

    @Transactional
    public Order payOrder(Long id, String email) {
        var order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        var client = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Objects.equals(order.getClientId(), client.getId())) {
            throw new IllegalArgumentException("No permission to access this order");
        }
        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot be paid.");
        }
        order.setOrderStatus(OrderStatus.PAID);
        return orderRepository.save(order);
    }

    public List<Order> getAllByStatus(OrderStatus status) {
        var result = orderRepository.findAllByOrderStatus(status);
        if (result == null) {
            throw new IllegalStateException("Orders with this status don't exist");
        }
        return result;
    }
}
