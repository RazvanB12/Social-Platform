package com.disi.social_platform_be.dto.responses;

import com.disi.social_platform_be.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private UUID userId;
    private Role role;
    private String Name;
}
