package com.disi.social_platform_be.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateImageDto {

    @NotNull
    private String imageId;

    @NotNull
    private String publicImage;
}
