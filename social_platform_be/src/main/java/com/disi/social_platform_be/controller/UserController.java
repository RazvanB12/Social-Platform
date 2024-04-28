package com.disi.social_platform_be.controller;

import com.disi.social_platform_be.dto.requests.LoginDto;
import com.disi.social_platform_be.dto.requests.PasswordResetDto;
import com.disi.social_platform_be.dto.requests.RegisterDto;
import com.disi.social_platform_be.dto.responses.ClientDtoResponse;
import com.disi.social_platform_be.dto.responses.LoginResponseDto;
import com.disi.social_platform_be.dto.responses.Response;
import com.disi.social_platform_be.dto.responses.UserDtoResponse;
import com.disi.social_platform_be.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final IUserService userService;

    @GetMapping("admin/clients")
    public ResponseEntity<Response<List<ClientDtoResponse>>> getAllClients() {
        var response = userService.getAllClients();
        return ResponseEntity.ok(new Response<>(response));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponseDto>> login(@Valid @RequestBody LoginDto loginDto) {
        var loginResponseDto = userService.login(loginDto);
        return ResponseEntity.ok(new Response<>(loginResponseDto));
    }

    @GetMapping("/pending-users")
    public ResponseEntity<Response<List<UserDtoResponse>>> getPendingUsers() {
        var pendingUsers = userService.getAllPendingUsers();
        return ResponseEntity.ok(new Response<>(pendingUsers));
    }

    @PostMapping("/register")
    public ResponseEntity<Response<UserDtoResponse>> register(@Valid @RequestBody RegisterDto registerDto) {
        var registeredUser = userService.register(registerDto);
        return ResponseEntity.ok(new Response<>(registeredUser));
    }

    @PatchMapping("/activate-client-account/{id}")
    public ResponseEntity<Response<String>> activateClientAccount(@PathVariable("id") UUID clientId) {
        var response = userService.activateClientAccount(clientId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PatchMapping("/reject-client-account/{id}")
    public ResponseEntity<Response<String>> rejectClientAccount(@PathVariable("id") UUID clientId) {
        var response = userService.rejectClientAccount(clientId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<Response<String>> forgotPassword(@PathVariable("email") String email) {
        var response = userService.forgotPassword(email);
        return ResponseEntity.ok(new Response<>(response));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<UserDtoResponse>> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) {
        var response = userService.resetPassword(passwordResetDto);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/admin/delete-client-account/{id}")
    public ResponseEntity<Response<String>> deleteClientAccount(@PathVariable("id") UUID clientId) {
        var response = userService.deleteClientAccount(clientId);
        return ResponseEntity.ok(new Response<>(response));
    }

    @DeleteMapping("/admin/delete-client-image/{id}")
    public ResponseEntity<Response<String>> deleteClientImage(@PathVariable("id") UUID imageId) {
        var response = userService.deleteClientImage(imageId);
        return ResponseEntity.ok(new Response<>(response));
    }
}
