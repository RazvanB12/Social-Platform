package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.LoginDto;
import com.disi.social_platform_be.dto.requests.PasswordResetDto;
import com.disi.social_platform_be.dto.requests.RegisterDto;
import com.disi.social_platform_be.dto.responses.ClientDtoResponse;
import com.disi.social_platform_be.dto.responses.LoginResponseDto;
import com.disi.social_platform_be.dto.responses.UserDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    LoginResponseDto login(LoginDto loginDto);

    List<UserDtoResponse> getAllPendingUsers();

    List<ClientDtoResponse> getAllClients();

    UserDtoResponse register(RegisterDto registerDto);

    String activateClientAccount(UUID clientId);

    String rejectClientAccount(UUID clientId);

    String forgotPassword(String email);

    UserDtoResponse resetPassword(PasswordResetDto passwordResetDto);

    String deleteClientAccount(UUID clientId);

    String deleteClientImage(UUID imageId);
}
