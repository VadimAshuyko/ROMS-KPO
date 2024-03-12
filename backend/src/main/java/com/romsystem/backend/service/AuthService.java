package com.romsystem.backend.service;

import com.romsystem.backend.dto.*;
import com.romsystem.backend.dto.AuthDto.LogInRequestDTO;
import com.romsystem.backend.dto.AuthDto.LogInResponseDTO;
import com.romsystem.backend.dto.AuthDto.SignUpRequestDTO;
import com.romsystem.backend.dto.AuthDto.SignUpResponseDTO;
import com.romsystem.backend.entity.User;
import com.romsystem.backend.enums.UserRole;
import com.romsystem.backend.repository.UserRepository;
import com.romsystem.backend.util.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> createNewUser(SignUpRequestDTO signupRequestDTO) {
        Optional<User> candidate = userRepository.findByEmail(signupRequestDTO.getEmail());
        if (candidate.isPresent()) {
            return new ResponseEntity<>(new ErrorResponseDTO("этот email занят"), HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(signupRequestDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setUsername(signupRequestDTO.getUsername());
        user.setRole(UserRole.ROLE_CUSTOMER);
        userService.save(user);
        UserDetails userDetails = userService.loadUserByUsername(signupRequestDTO.getEmail());
        String accessToken = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(new SignUpResponseDTO(accessToken, userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new)), HttpStatus.OK);
    }
    public ResponseEntity<?> createAuthToken(LogInRequestDTO loginRequestDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponseDTO("Неверный логин или пароль"), HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = userService.loadUserByUsername(loginRequestDTO.getEmail());
        String accessToken = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(new LogInResponseDTO(accessToken, userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new)
        ), HttpStatus.OK);
    }
}
