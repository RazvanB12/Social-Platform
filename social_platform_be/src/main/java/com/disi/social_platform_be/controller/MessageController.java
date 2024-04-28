package com.disi.social_platform_be.controller;

import com.disi.social_platform_be.dto.requests.MessageDtoRequest;
import com.disi.social_platform_be.dto.responses.MessageDtoResponse;
import com.disi.social_platform_be.dto.responses.Response;
import com.disi.social_platform_be.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    private final IMessageService messageService;

    @GetMapping("messages/{id}")
    public ResponseEntity<Response<List<MessageDtoResponse>>> getMessages(@PathVariable("id") UUID userId) {
        var response = messageService.getMessages(userId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PostMapping("messages")
    public ResponseEntity<Response<String>> sendMessage(@RequestBody MessageDtoRequest messageDto) {
        var response = messageService.sendMessage(messageDto);
        return ResponseEntity.ok(new Response<>(response));
    }
}
