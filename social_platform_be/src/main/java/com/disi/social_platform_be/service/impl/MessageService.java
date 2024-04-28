package com.disi.social_platform_be.service.impl;

import com.disi.social_platform_be.dto.mapper.MessageMapper;
import com.disi.social_platform_be.dto.requests.MessageDtoRequest;
import com.disi.social_platform_be.dto.responses.MessageDtoResponse;
import com.disi.social_platform_be.exception.UnfriendException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Message;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.Role;
import com.disi.social_platform_be.repository.IFriendRequestRepository;
import com.disi.social_platform_be.repository.IMessageRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.IMessageService;
import com.disi.social_platform_be.util.AuthenticationService;
import com.disi.social_platform_be.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;
    private final IFriendRequestRepository friendRequestRepository;
    private final AuthenticationService authenticationService;
    private final TokenHolder tokenHolder;

    private final static String USER_NOT_FOUND = "User does not exists";
    private final static String UNFRIEND_EXCEPTION = "Users must be friends";

    @Override
    public String sendMessage(MessageDtoRequest messageDto) {
        List<User> users = userRepository.findAllByRoleAndActive(Role.CLIENT, true);

        User currentUser = getUserById(users, authenticationService.extractId(tokenHolder.getToken()));
        User toUser = getUserById(users, messageDto.getToUserId());
        checkFriend(currentUser, toUser);

        Message message = MessageMapper.mapToMessage(messageDto.getContent(), currentUser, toUser);
        messageRepository.save(message);
        return "Sent message successfully";
    }

    @Override
    public List<MessageDtoResponse> getMessages(UUID userId) {
        List<User> users = userRepository.findAllByRoleAndActive(Role.CLIENT, true);
        User currentUser = getUserById(users, authenticationService.extractId(tokenHolder.getToken()));
        User toUser = getUserById(users, userId);
        return messageRepository.getAllMessagesForUser(currentUser, toUser)
                .stream()
                .map(m -> MessageMapper.mapToMessageDtoResponse(m, currentUser))
                .toList();
    }

    private void checkFriend(User currentUser, User toUser) {
        friendRequestRepository.findAllByFromUserOrToUser(currentUser, currentUser)
                .stream()
                .filter(friendRequest ->
                        (friendRequest.getToUser().getId().equals(toUser.getId())
                                || friendRequest.getFromUser().getId().equals(toUser.getId()))
                                && friendRequest.getApproved())
                .findAny()
                .orElseThrow(() -> new UnfriendException(UNFRIEND_EXCEPTION));
    }

    private User getUserById(List<User> users, UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }
}
