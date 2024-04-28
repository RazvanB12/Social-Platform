package com.disi.social_platform_be.controller;

import com.disi.social_platform_be.dto.responses.FriendRequestDto;
import com.disi.social_platform_be.dto.responses.Response;
import com.disi.social_platform_be.service.IFriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FriendRequestController {

    private final IFriendRequestService friendRequestService;

    @GetMapping("/friends")
    public ResponseEntity<Response<List<FriendRequestDto>>> getFriends() {
        var response = friendRequestService.getFriends();
        return ResponseEntity.ok(new Response<>(response));
    }

    @PostMapping("/friend-requests/{id}")
    public ResponseEntity<Response<String>> createFriendRequest(@PathVariable("id") UUID friendId) {
        var response = friendRequestService.createFriendRequest(friendId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @GetMapping("/friend-requests")
    public ResponseEntity<Response<List<FriendRequestDto>>> getFriendRequests() {
        var friendRequests = friendRequestService.getFriendRequests();
        return ResponseEntity.ok(new Response<>(friendRequests));
    }

    @PatchMapping("/accept-friend-request/{id}")
    public ResponseEntity<Response<String>> acceptFriendRequest(@PathVariable("id")UUID friendId) {
        var response = friendRequestService.acceptFriendRequest(friendId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/reject-friend-request/{id}")
    public ResponseEntity<Response<String>> rejectFriendRequest(@PathVariable("id")UUID friendId) {
        var response = friendRequestService.rejectFriendRequest(friendId);
        return ResponseEntity.ok(new Response<>(response));
    }
}
