package com.disi.social_platform_be.service.impl;

import com.disi.social_platform_be.dto.mapper.FriendRequestMapper;
import com.disi.social_platform_be.dto.responses.FriendRequestDto;
import com.disi.social_platform_be.exception.FriendRequestException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.FriendRequest;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.FriendStatus;
import com.disi.social_platform_be.model.enums.Role;
import com.disi.social_platform_be.repository.IDetailRepository;
import com.disi.social_platform_be.repository.IFriendRequestRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.IFriendRequestService;
import com.disi.social_platform_be.util.AuthenticationService;
import com.disi.social_platform_be.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendRequestService implements IFriendRequestService {

    private final IUserRepository userRepository;
    private final IDetailRepository detailRepository;
    private final IFriendRequestRepository friendRequestRepository;
    private final AuthenticationService authenticationService;
    private final TokenHolder tokenHolder;

    private static final String INVALID_USER = "Invalid user";
    private static final String INVALID_FRIEND_REQUEST = "Friend request does not exist";
    private static final String EXISTING_FRIEND_REQUEST = "Friend request already exists";
    private static final String APPROVED_FRIEND_REQUEST = "Friend request already approved";

    @Override
    public String createFriendRequest(UUID friendId) {
        Optional<User> currentUser = userRepository.findById(authenticationService.extractId(tokenHolder.getToken()));
        Optional<User> friend = userRepository.findById(friendId);

        if (currentUser.isEmpty() || friend.isEmpty() || !currentUser.get().getActive() || !friend.get().getActive()
        || currentUser.get().getRole() == Role.ADMIN || friend.get().getRole() == Role.ADMIN) {
            throw new UserNotFoundException(INVALID_USER);
        }

        var alreadyFriends = !friendRequestRepository.findAllByFromUserAndToUser(currentUser.get(), friend.get()).isEmpty()
                || !friendRequestRepository.findAllByFromUserAndToUser(currentUser.get(), friend.get()).isEmpty();

        if (alreadyFriends) {
            throw new FriendRequestException(EXISTING_FRIEND_REQUEST);
        }

        FriendRequest friendRequest =  new FriendRequest(currentUser.get(), friend.get());
        friendRequestRepository.save(friendRequest);
        return "Friend request successfully created";
    }

    @Override
    public List<FriendRequestDto> getFriendRequests() {
        List<User> users = userRepository.findAllByRoleAndActive(Role.CLIENT, true);
        User currentUser = users.stream()
                .filter(user -> user.getId().equals(authenticationService.extractId(tokenHolder.getToken())))
                .findFirst()
                .get();
        users.remove(currentUser);

        List<FriendRequest> friendRequests = friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser);
        List<Detail> details = detailRepository.findAllByUserIn(users);

        return users.stream()
                .map(user -> mapToFriendRequestDto(user, currentUser, friendRequests, details))
                .toList();
    }

    @Override
    public List<FriendRequestDto> getFriends() {
        List<User> users = userRepository.findAllByRoleAndActive(Role.CLIENT, true);
        User currentUser = users.stream()
                .filter(user -> user.getId().equals(authenticationService.extractId(tokenHolder.getToken())))
                .findFirst()
                .get();
        users.remove(currentUser);

        List<FriendRequest> friendRequests = friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser);
        List<Detail> details = detailRepository.findAllByUserIn(users);

        return friendRequests.stream()
                .filter(FriendRequest::getApproved)
                .map(friendRequest -> mapToFriendRequestDto(currentUser, friendRequest, details))
                .toList();
    }

    @Override
    public String acceptFriendRequest(UUID friendId) {
        Optional<FriendRequest> friendRequest = getFriendRequest(friendId);
        if (friendRequest.isEmpty()) {
            throw new FriendRequestException(INVALID_FRIEND_REQUEST);
        }
        if (friendRequest.get().getApproved()) {
            throw new FriendRequestException(APPROVED_FRIEND_REQUEST);
        }

        FriendRequest acceptedFriendRequest = friendRequest.get();
        acceptedFriendRequest.setApproved(true);
        friendRequestRepository.save(acceptedFriendRequest);
        return "Friend request accepted";
    }

    @Override
    public String rejectFriendRequest(UUID friendId) {
        Optional<FriendRequest> friendRequest = getFriendRequest(friendId);
        if (friendRequest.isEmpty()) {
            throw new FriendRequestException(INVALID_FRIEND_REQUEST);
        }
        if (friendRequest.get().getApproved()) {
            throw new FriendRequestException(APPROVED_FRIEND_REQUEST);
        }

        FriendRequest deletedFriendRequest = friendRequest.get();
        friendRequestRepository.delete(deletedFriendRequest);
        return "Friend request rejected";
    }

    private FriendRequestDto mapToFriendRequestDto(User user, User currentUser, List<FriendRequest> friendRequests, List<Detail> details) {
        Optional<FriendRequest> friendRequest = friendRequests.stream()
                .filter(fr ->
                        (fr.getFromUser().getId().equals(currentUser.getId()) && fr.getToUser().getId().equals(user.getId()))
                        ||
                        (fr.getFromUser().getId().equals(user.getId()) && fr.getToUser().getId().equals(currentUser.getId())))
                .findFirst();
        Optional<Detail> optionalDetail = details.stream()
                .filter(d -> d.getUser().getId().equals(user.getId()))
                .findFirst();
        var detail = optionalDetail.orElseGet(Detail::new);

        if (friendRequest.isEmpty()) {
            return FriendRequestMapper.mapToFriendRequestDto(user, detail, FriendStatus.NOT_FRIEND);
        }
        if (friendRequest.get().getFromUser().getId().equals(currentUser.getId()) && !friendRequest.get().getApproved()) {
            return FriendRequestMapper.mapToFriendRequestDto(user, detail, FriendStatus.SENT_FRIEND_REQUEST);
        }
        if (friendRequest.get().getToUser().getId().equals(currentUser.getId()) && !friendRequest.get().getApproved()) {
            return FriendRequestMapper.mapToFriendRequestDto(user, detail, FriendStatus.PENDING_FRIEND);
        }
        if (friendRequest.get().getApproved()) {
            return FriendRequestMapper.mapToFriendRequestDto(user, detail, FriendStatus.FRIEND);
        }

        return null;
    }

    private FriendRequestDto mapToFriendRequestDto(User currentUser, FriendRequest friendRequest, List<Detail> details) {
        User friend = getFriend(currentUser, friendRequest);
        Optional<Detail> optionalDetail = details.stream()
                .filter(d -> d.getUser().getId().equals(friend.getId()))
                .findFirst();
        Detail detail = optionalDetail.orElseGet(Detail::new);

        return FriendRequestMapper.mapToFriendRequestDto(friend, detail, FriendStatus.FRIEND);
    }

    private Optional<FriendRequest> getFriendRequest(UUID friendId) {
        Optional<User> currentUser = userRepository.findById(authenticationService.extractId(tokenHolder.getToken()));
        Optional<User> friend = userRepository.findById(friendId);

        if (currentUser.isEmpty() || friend.isEmpty()) {
            throw new UserNotFoundException(INVALID_USER);
        }

        return friendRequestRepository.findAllByFromUserAndToUser(friend.get(), currentUser.get())
                .stream()
                .findFirst();
    }

    private User getFriend(User currentUser, FriendRequest friendRequest) {
        if (friendRequest.getToUser().getId().equals(currentUser.getId())) {
            return friendRequest.getFromUser();
        }
        return friendRequest.getToUser();
    }
}
