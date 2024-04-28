package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.MessageDtoRequest;
import com.disi.social_platform_be.dto.responses.MessageDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IMessageService {

    String sendMessage(MessageDtoRequest messageDto);

    List<MessageDtoResponse> getMessages(UUID userId);
}
