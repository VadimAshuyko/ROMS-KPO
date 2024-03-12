package com.romsystem.backend.controller;

import com.romsystem.backend.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/revenue")

public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping("/getRevenue")
    public ResponseEntity<BigDecimal> getRevenue() {
        return ResponseEntity.ok(revenueService.getRevenueAtAllTime());
    }
}
