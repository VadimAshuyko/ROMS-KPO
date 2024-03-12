package com.romsystem.backend.repository;

import com.romsystem.backend.entity.DishFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishFeedbackRepository extends JpaRepository<DishFeedback, Long> {
    List<DishFeedback> findAll();

    List<DishFeedback> findAllByAuthorId(Long authorId);

    List<DishFeedback> findAllByDishId(Long dishId);
}
