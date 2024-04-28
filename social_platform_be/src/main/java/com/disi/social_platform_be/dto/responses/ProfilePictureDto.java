package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfilePictureDto {

    private byte[] content;
    private String extension;
}
