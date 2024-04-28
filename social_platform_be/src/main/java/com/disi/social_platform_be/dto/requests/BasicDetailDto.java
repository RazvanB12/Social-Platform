package com.disi.social_platform_be.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicDetailDto {

    @NotNull
    private String description;

    @NotNull
    private String address;

    @NotNull
    private String hobbies;

    @NotNull
    private Boolean publicDetails;
}
