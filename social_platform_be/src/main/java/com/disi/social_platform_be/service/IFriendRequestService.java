package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.responses.FriendRequestDto;

import java.util.List;
import java.util.UUID;

public interface IFriendRequestService {

    String createFriendRequest(UUID friendId);

    List<FriendRequestDto> getFriendRequests();

    List<FriendRequestDto> getFriends();

    String acceptFriendRequest(UUID friendId);

    String rejectFriendRequest(UUID friendId);
}
