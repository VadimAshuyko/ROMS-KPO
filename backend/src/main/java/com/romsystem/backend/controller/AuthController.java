package com.romsystem.backend.controller;


import com.romsystem.backend.dto.AuthDto.LogInRequestDTO;
import com.romsystem.backend.dto.AuthDto.SignUpRequestDTO;
import com.romsystem.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody SignUpRequestDTO signupRequestDTO) {
        return authService.createNewUser(signupRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody LogInRequestDTO loginRequestDTO) {
        return authService.createAuthToken(loginRequestDTO);
    }
}
