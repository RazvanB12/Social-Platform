package com.disi.social_platform_be.dto.mapper;

import com.disi.social_platform_be.dto.responses.FriendRequestDto;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.FriendRequest;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.FriendStatus;

public class FriendRequestMapper {

    public static FriendRequestDto mapToFriendRequestDto(User user, Detail detail, FriendStatus friendStatus) {
        return new FriendRequestDto(
                user.getId(),
                String.format("%s %s", user.getFirstName(), user.getLastName()),
                detail.getProfilePictureContent(),
                detail.getProfilePictureExtension(),
                friendStatus
                );
    }
}
