package com.romsystem.backend.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Data
@Validated
public class OrderDTO {
    private Map<Long, Integer> dishes;
}