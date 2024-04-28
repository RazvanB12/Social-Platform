package com.disi.social_platform_be.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageDtoRequest {
    private String content;
    private UUID toUserId;
}
