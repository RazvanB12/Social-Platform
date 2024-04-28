package com.disi.social_platform_be.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordResetDto {

    @NotEmpty
    @NotNull
    private String userToken;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "must contain at least one letter, one number, one special character, and have a minimum length of 8")
    private String password;
}
