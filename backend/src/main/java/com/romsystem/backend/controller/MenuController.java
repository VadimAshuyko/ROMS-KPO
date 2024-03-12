package com.romsystem.backend.controller;

import com.romsystem.backend.dto.MenuResponseDTO;
import com.romsystem.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/getMenu")
    public ResponseEntity<MenuResponseDTO> getMenu() {
        return menuService.getMenu();
    }

    @GetMapping("/getDish/{dishId}")
    public ResponseEntity<?> getDish(@PathVariable Long dishId) {
        return menuService.getDish(dishId);
    }
}
