package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.MessageDtoRequest;
import com.disi.social_platform_be.exception.UnfriendException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.FriendRequest;
import com.disi.social_platform_be.model.Message;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.MessageType;
import com.disi.social_platform_be.model.enums.Role;
import com.disi.social_platform_be.repository.IFriendRequestRepository;
import com.disi.social_platform_be.repository.IMessageRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.impl.MessageService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private IMessageRepository messageRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IFriendRequestRepository friendRequestRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private TokenHolder tokenHolder;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void givenExistingUsers_whenSendMessage_thenReturnSuccessfullyMessage() {
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        UUID friendUserId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendUserId);
        MessageDtoRequest messageDto = new MessageDtoRequest("messageContent", friendUserId);
        String token = "token";
        List<User> users = List.of(currentUser, friendUser);
        List<FriendRequest> friendRequests = List.of(new FriendRequest(UUID.randomUUID(), true, currentUser, friendUser));

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when( friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser)).thenReturn(friendRequests);
        when(messageRepository.save(any())).thenReturn(null);
        var response = messageService.sendMessage(messageDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Sent message successfully", response);
    }

    @Test
    public void givenNotExistingUser_whenSendMessage_thenThrowException() {
        UUID currentUserId = UUID.randomUUID();
        UUID friendUserId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendUserId);
        MessageDtoRequest messageDto = new MessageDtoRequest("messageContent", friendUserId);
        String token = "token";
        List<User> users = List.of(friendUser);

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);

        Assertions.assertThrows(UserNotFoundException.class, () -> messageService.sendMessage(messageDto));
    }

    @Test
    public void givenNotExistingFriend_whenSendMessage_thenThrowException() {
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        UUID friendUserId = UUID.randomUUID();
        MessageDtoRequest messageDto = new MessageDtoRequest("messageContent", friendUserId);
        String token = "token";
        List<User> users = List.of(currentUser);

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);

        Assertions.assertThrows(UserNotFoundException.class, () -> messageService.sendMessage(messageDto));
    }

    @Test
    public void givenExistingUsersButNotFriends_whenSendMessage_thenThrowException() {
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        UUID friendUserId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendUserId);
        MessageDtoRequest messageDto = new MessageDtoRequest("messageContent", friendUserId);
        String token = "token";
        List<User> users = List.of(currentUser, friendUser);
        List<FriendRequest> friendRequests = new ArrayList<>();

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when( friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser)).thenReturn(friendRequests);

       Assertions.assertThrows(UnfriendException.class, () -> messageService.sendMessage(messageDto));
    }

    @Test
    public void givenExistingUser_whenGetMessages_ThenReturnMessageDtoList() {
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        UUID friendUserId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendUserId);
        String token = "token";
        List<User> users = List.of(currentUser, friendUser);
        List<Message> messages = List.of(
                new Message(UUID.randomUUID(), "content", 1234L, currentUser, friendUser),
                new Message(UUID.randomUUID(), "content", 1234L, currentUser, friendUser),
                new Message(UUID.randomUUID(), "content", 1234L, friendUser, currentUser),
                new Message(UUID.randomUUID(), "content", 1234L, friendUser, currentUser)
        );

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);
        when(messageRepository.getAllMessagesForUser(currentUser, friendUser)).thenReturn(messages);
        var response = messageService.getMessages(friendUserId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(4, response.size());
        Assertions.assertEquals(2, response.stream().filter(r -> r.getMessageType().equals(MessageType.SENT)).toList().size());
        Assertions.assertEquals(2, response.stream().filter(r -> r.getMessageType().equals(MessageType.RECEIVED)).toList().size());
    }

    @Test
    public void givenNotExistingUser_whenGetMessages_ThenThrowException() {
        UUID currentUserId = UUID.randomUUID();
        UUID friendUserId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendUserId);
        String token = "token";
        List<User> users = List.of(friendUser);

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);

        Assertions.assertThrows(UserNotFoundException.class, () -> messageService.getMessages(friendUserId));
    }

    @Test
    public void givenNotExistingFriend_whenGetMessages_ThenThrowException() {
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        UUID friendUserId = UUID.randomUUID();
        String token = "token";
        List<User> users = List.of(currentUser);

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, true)).thenReturn(users);
        when(tokenHolder.getToken()).thenReturn(token);
        when(authenticationService.extractId(token)).thenReturn(currentUserId);

        Assertions.assertThrows(UserNotFoundException.class, () -> messageService.getMessages(friendUserId));
    }
}
