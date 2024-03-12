package com.romsystem.backend.dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogInResponseDTO {
    private String accessToken;
    private String[] authorities;
}