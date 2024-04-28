package com.disi.social_platform_be.service;

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
import com.disi.social_platform_be.service.impl.FriendRequestService;
import com.disi.social_platform_be.util.AuthenticationService;
import com.disi.social_platform_be.util.TestDataBuilder;
import com.disi.social_platform_be.util.TokenHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendRequestServiceTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IDetailRepository detailRepository;
    @Mock
    private IFriendRequestRepository friendRequestRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private TokenHolder tokenHolder;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Test
    public void givenExistingUsers_whenCreateFriendRequest_thenReturnSuccessfullyMessage() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(any(), any())).thenReturn(new ArrayList<>());
        when(friendRequestRepository.findAllByFromUserAndToUser(any(), any())).thenReturn(new ArrayList<>());
        when(friendRequestRepository.save(any())).thenReturn(null);

        var response = friendRequestService.createFriendRequest(friendId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Friend request successfully created", response);
    }

    @Test
    public void givenNotExistingUser_whenCreateFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.createFriendRequest(friendId));
    }

    @Test
    public void givenNotExistingFriend_whenCreateFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.createFriendRequest(friendId));
    }

    @Test
    public void givenNotActivatedUser_whenCreateFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        currentUser.setActive(false);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.createFriendRequest(friendId));
    }

    @Test
    public void givenAdminUser_whenCreateFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        currentUser.setRole(Role.ADMIN);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.createFriendRequest(friendId));
    }

    @Test
    public void givenExistingFriends_whenCreateFriendRequest_thenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";
        List<FriendRequest> friendRequestList = List.of( new FriendRequest(currentUser, friendUser));

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(any(), any())).thenReturn(friendRequestList);

        Assertions.assertThrows(FriendRequestException.class, () -> friendRequestService.createFriendRequest(friendId));
    }

    @Test
    public void givenExistingUser_whenGetFriendRequests_thenReturnFriendRequestDtoList() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";
        List<User> users = new ArrayList<>();
        users.add(currentUser);
        users.add(friendUser);
        List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), true, currentUser, friendUser));
        List<Detail> details = new ArrayList<>();

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser)).thenReturn(friendRequests);
        when(detailRepository.findAllByUserIn(users)).thenReturn(details);

        var response = friendRequestService.getFriendRequests();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(FriendStatus.FRIEND, response.get(0).getFriendRequestStatus());
        Assertions.assertEquals(friendId, response.get(0).getUserId());
    }

    @Test
    public void givenExistingUser_whenGetFriends_thenReturnFriendRequestDtoList() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";
        List<User> users = new ArrayList<>();
        users.add(currentUser);
        users.add(friendUser);
        List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), true, currentUser, friendUser));
        List<Detail> details = new ArrayList<>();

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser)).thenReturn(friendRequests);
        when(detailRepository.findAllByUserIn(users)).thenReturn(details);

        var response = friendRequestService.getFriends();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(FriendStatus.FRIEND, response.get(0).getFriendRequestStatus());
        Assertions.assertEquals(friendId, response.get(0).getUserId());
    }

    @Test
    public void givenExistingUsers_whenAcceptFriendRequest_ReturnSuccessfullyMessage() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";
        List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), false, currentUser, friendUser));

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(friendUser, currentUser)).thenReturn(friendRequests);
        when(friendRequestRepository.save(any())).thenReturn(null);
        var response = friendRequestService.acceptFriendRequest(friendId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Friend request accepted", response);
    }

    @Test
    public void givenNotExistingUser_whenAcceptFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.acceptFriendRequest(friendId));
    }

    @Test
    public void givenNotExistingFriend_whenAcceptFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.acceptFriendRequest(friendId));
    }

    @Test
    public void givenNotExistingFriendRequest_whenAcceptFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(friendUser, currentUser)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(FriendRequestException.class, () -> friendRequestService.acceptFriendRequest(friendId));
    }

    @Test
    public void givenAcceptedFriendRequest_whenAcceptFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";
        List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), true, currentUser, friendUser));

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(friendUser, currentUser)).thenReturn(friendRequests);

        Assertions.assertThrows(FriendRequestException.class, () -> friendRequestService.acceptFriendRequest(friendId));
    }

     @Test
     public void givenExistingUsers_whenRejectFriendRequest_ReturnSuccessfullyMessage() {
         UUID friendId = UUID.randomUUID();
         User friendUser = TestDataBuilder.createUser(friendId);
         UUID currentUserId = UUID.randomUUID();
         User currentUser = TestDataBuilder.createUser(currentUserId);
         String token = "token";
         List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), false, currentUser, friendUser));

         when(tokenHolder.getToken()).thenReturn(token);
         when(authenticationService.extractId(token)).thenReturn(currentUserId);
         when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
         when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
         when(friendRequestRepository.findAllByFromUserAndToUser(friendUser, currentUser)).thenReturn(friendRequests);
         doNothing().when(friendRequestRepository).delete(any());
         var response = friendRequestService.rejectFriendRequest(friendId);

         Assertions.assertNotNull(response);
         Assertions.assertEquals("Friend request rejected", response);
     }

    @Test
    public void givenNotExistingUser_whenRejectFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.rejectFriendRequest(friendId));
    }

    @Test
    public void givenNotExistingFriend_whenRejectFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> friendRequestService.rejectFriendRequest(friendId));
    }

    @Test
    public void givenNotExistingFriendRequest_whenRejectFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(friendUser, currentUser)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(FriendRequestException.class, () -> friendRequestService.rejectFriendRequest(friendId));
    }

    @Test
    public void givenAcceptedFriendRequest_whenRejectFriendRequest_ThenThrowException() {
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";
        List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), true, currentUser, friendUser));

        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friendUser));
        when(friendRequestRepository.findAllByFromUserAndToUser(friendUser, currentUser)).thenReturn(friendRequests);

        Assertions.assertThrows(FriendRequestException.class, () -> friendRequestService.rejectFriendRequest(friendId));
    }
}
