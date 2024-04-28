package com.disi.social_platform_be.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicAlbumDto {

    @NotNull
    private String userId;

    @NotNull
    private String name;
}
