package com.disi.social_platform_be.dto.mapper;

import com.disi.social_platform_be.dto.responses.MessageDtoResponse;
import com.disi.social_platform_be.model.Message;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.MessageType;

public class MessageMapper {

    public static Message mapToMessage(String content, User currentUser, User toUser) {
        return new Message(content, System.currentTimeMillis(), currentUser, toUser);
    }

    public static MessageDtoResponse mapToMessageDtoResponse(Message message, User currentUser) {
        return new MessageDtoResponse(
                message.getId(),
                message.getContent(),
                message.getDate(),
                message.getSender().getId().equals(currentUser.getId()) ? MessageType.SENT : MessageType.RECEIVED
        );
    }
}
