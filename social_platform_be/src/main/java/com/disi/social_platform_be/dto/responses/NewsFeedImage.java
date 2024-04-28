package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsFeedImage {

    private UserDetailDto user;
    private ProfilePictureDto profilePicture;
    private ImageDetailDto image;
}

