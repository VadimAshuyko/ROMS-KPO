package com.romsystem.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ErrorResponseDTO {
    private String message;
    private Date timestamp;

    public ErrorResponseDTO(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}
