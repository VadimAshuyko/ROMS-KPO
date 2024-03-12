package com.romsystem.backend.service;

import com.romsystem.backend.dto.DishFeedbackDTO;
import com.romsystem.backend.entity.DishFeedback;
import com.romsystem.backend.repository.DishFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishFeedbackService {
    private final DishFeedbackRepository dishFeedbackRepository;
    private final UserService userService;
    private final DishService dishService;

    @Transactional
    public DishFeedback create(Long dishId, DishFeedbackDTO dto) {
        var dish = dishService.get(dishId);
        if (!dish.isInMenu()) {
            throw new IllegalStateException("dish is not in menu");
        }
        DishFeedback feedback = new DishFeedback();
        feedback.setAuthorId(getAuthorId());
        feedback.setGrade(dto.getGrade());
        feedback.setDishId(dishId);
        feedback.setComment(dto.getComment());
        feedback = dishFeedbackRepository.save(feedback);
        return feedback;
    }

    public List<DishFeedback> getByAuthorId(Long authorId) {
        return dishFeedbackRepository.findAllByAuthorId(authorId);
    }

    public List<DishFeedback> getByDishId(Long dishId) {
        return dishFeedbackRepository.findAllByDishId(dishId);
    }

    public List<DishFeedback> getAll() {
        return dishFeedbackRepository.findAll();
    }

    private Long getAuthorId() {
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByEmail(user).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
    }
}
