package com.disi.social_platform_be.dto.responses;

import com.disi.social_platform_be.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDtoResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean publicContent;
}
