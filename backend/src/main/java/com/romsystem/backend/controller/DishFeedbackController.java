package com.romsystem.backend.controller;


import com.romsystem.backend.entity.DishFeedback;
import com.romsystem.backend.service.DishFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class DishFeedbackController {
    private final DishFeedbackService dishFeedbackService;

    @GetMapping("/getAllByAuthor/{id}")
    public ResponseEntity<List<DishFeedback>> getAllByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(dishFeedbackService.getByAuthorId(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DishFeedback>> getAll() {
        return ResponseEntity.ok(dishFeedbackService.getAll());
    }

    @GetMapping("/dish/{id}")
    public ResponseEntity<List<DishFeedback>> getAllByDish(@PathVariable Long id) {
        return ResponseEntity.ok(dishFeedbackService.getByDishId(id));
    }
}
