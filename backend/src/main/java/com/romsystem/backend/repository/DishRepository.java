package com.romsystem.backend.repository;

import com.romsystem.backend.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAll();
    List<Dish> findAllByIsInMenu(boolean isInMenu);
}
