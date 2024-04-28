package com.disi.social_platform_be.dto.mapper;

import com.disi.social_platform_be.dto.responses.DetailDto;
import com.disi.social_platform_be.dto.responses.ProfilePictureDto;
import com.disi.social_platform_be.model.Detail;

public class DetailMapper {

    public static DetailDto mapToDetailDto(Detail detail) {
        return new DetailDto(detail.getDescription(), detail.getAddress(), detail.getHobbies(),
                detail.getProfilePictureContent(), detail.getProfilePictureExtension(), detail.getPublicDetails());
    }

    public static ProfilePictureDto mapToProfilePictureDto(Detail detail) {
        return new ProfilePictureDto(detail.getProfilePictureContent(), detail.getProfilePictureExtension());
    }
}
