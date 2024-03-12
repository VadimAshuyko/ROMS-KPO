package com.romsystem.backend.dto;

import com.romsystem.backend.enums.Grade;
import lombok.Data;


@Data
public class DishFeedbackDTO {
    private Grade grade;
    private String comment;
}