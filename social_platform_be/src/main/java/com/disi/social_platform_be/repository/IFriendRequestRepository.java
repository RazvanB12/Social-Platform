package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.FriendRequest;
import com.disi.social_platform_be.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface IFriendRequestRepository extends CrudRepository<FriendRequest, UUID> {

    List<FriendRequest> findAllByFromUserOrToUser(User fromUser, User toUser);

    List<FriendRequest> findAllByFromUserAndToUser(User fromUser, User toUser);

    List<FriendRequest> findAllByFromUserAndApprovedOrToUserAndApproved(User fromUser, Boolean approvedFrom, User toUser, Boolean approvedTo);
}
