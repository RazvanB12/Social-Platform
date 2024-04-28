package com.disi.social_platform_be.dto.responses;

import com.disi.social_platform_be.model.enums.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FriendRequestDto {

    private UUID userId;
    private String name;
    private byte[] imageContent;
    private String imageExtension;
    private FriendStatus friendRequestStatus;
}
