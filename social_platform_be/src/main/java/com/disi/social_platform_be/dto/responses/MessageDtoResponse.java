package com.disi.social_platform_be.dto.responses;

import com.disi.social_platform_be.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageDtoResponse {
    private UUID id;
    private String content;
    private Long date;
    private MessageType messageType;
}
