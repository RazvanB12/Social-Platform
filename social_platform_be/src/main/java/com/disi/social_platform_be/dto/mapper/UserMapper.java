package com.disi.social_platform_be.dto.mapper;

import com.disi.social_platform_be.dto.requests.RegisterDto;
import com.disi.social_platform_be.dto.responses.ClientDtoResponse;
import com.disi.social_platform_be.dto.responses.LoginResponseDto;
import com.disi.social_platform_be.dto.responses.UserDetailDto;
import com.disi.social_platform_be.dto.responses.UserDtoResponse;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.Role;

public class UserMapper {

    public static LoginResponseDto mapToLoginResponseDto(User user, String token) {
        return new LoginResponseDto(token, user.getId(), user.getRole(), String.format("%s %s", user.getFirstName(), user.getLastName()));
    }

    public static UserDtoResponse mapToUserDtoResponse(User user) {
        return new UserDtoResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getRole(), user.getPublicContent());
    }

    public static User mapToUser(RegisterDto registerDto, Detail detail) {
        return new User(registerDto.getEmail(), registerDto.getFirstName(), registerDto.getLastName(),
                registerDto.getPassword(), Role.CLIENT, false, registerDto.getPublicContent(), detail);
    }

    public static UserDetailDto mapToUserDetailDto(User user) {
        return new UserDetailDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public static ClientDtoResponse mapToClientDtoResponse(User user) {
        return new ClientDtoResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getActive());
    }
}
