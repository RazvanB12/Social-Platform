package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailDto {

    private String description;
    private String address;
    private String hobbies;
    private byte[] profilePictureContent;
    private String profilePictureExtension;
    private Boolean publicDetails;
}
