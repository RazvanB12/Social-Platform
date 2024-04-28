package com.disi.social_platform_be.service.impl;

import com.disi.social_platform_be.dto.mapper.UserMapper;
import com.disi.social_platform_be.dto.requests.LoginDto;
import com.disi.social_platform_be.dto.requests.PasswordResetDto;
import com.disi.social_platform_be.dto.requests.RegisterDto;
import com.disi.social_platform_be.dto.responses.ClientDtoResponse;
import com.disi.social_platform_be.dto.responses.LoginResponseDto;
import com.disi.social_platform_be.dto.responses.UserDtoResponse;
import com.disi.social_platform_be.exception.*;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.Image;
import com.disi.social_platform_be.model.PasswordResetToken;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.Role;
import com.disi.social_platform_be.repository.IDetailRepository;
import com.disi.social_platform_be.repository.IImageRepository;
import com.disi.social_platform_be.repository.IPasswordResetTokenRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.IUserService;
import com.disi.social_platform_be.util.AuthenticationService;
import com.disi.social_platform_be.util.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IDetailRepository detailRepository;
    private final IPasswordResetTokenRepository tokenRepository;
    private final IImageRepository imageRepository;
    private final AuthenticationService authenticationService;
    private final MailSender mailSender;

    private final static String INVALID_CREDENTIALS = "Provided credentials are not valid";
    private final static String INACTIVE_ACCOUNT = "Inactive account";
    private final static String EMAIL_EXISTS = "Email address already registered";
    private final static String USER_NOT_FOUND = "User does not exists";
    private final static String ADMIN_EXCEPTION = "Admin account can not be activated";
    private final static String ALREADY_ACTIVATED_ACCOUNT = "The account has already been activated";
    private final static String INVALID_EMAIL = "User with this email does not exists";
    private final static String INVALID_RESET = "The reset token is invalid";
    private final static String EXPIRED_RESET = "The reset token is expired";
    private final static String DELETE_ADMIN_ACCOUNT = "Admin account can not be deleted";
    private final static String IMAGE_NOT_FOUND = "Image does not exist";

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())
                .orElseThrow(() -> new UserNotFoundException(INVALID_CREDENTIALS));

        if (!user.getActive()) {
            throw new InactiveAccountException(INACTIVE_ACCOUNT);
        }

        return UserMapper.mapToLoginResponseDto(user, authenticationService.generateToken(createClaims(user)));
    }

    @Override
    public List<UserDtoResponse> getAllPendingUsers() {
        return userRepository.findAllByRoleAndActive(Role.CLIENT, false)
                .stream()
                .map(UserMapper::mapToUserDtoResponse)
                .toList();
    }

    @Override
    public List<ClientDtoResponse> getAllClients() {
        return userRepository.findAllByRole(Role.CLIENT)
                .stream()
                .map(UserMapper::mapToClientDtoResponse)
                .toList();
    }

    @Override
    public UserDtoResponse register(RegisterDto registerDto) {
        try {
            Detail detail = new Detail();
            detailRepository.save(detail);
            return UserMapper.mapToUserDtoResponse(userRepository.save(UserMapper.mapToUser(registerDto, detail)));
        } catch (Exception exception) {
            throw new DuplicateEmailException(EMAIL_EXISTS);
        }
    }

    @Override
    public String activateClientAccount(UUID clientId) {
        User user = getInactiveClient(clientId);

        user.setActive(true);
        userRepository.save(user);
        return "Account successfully activated";
    }

    @Override
    public String rejectClientAccount(UUID clientId) {
        User user = getInactiveClient(clientId);

        userRepository.delete(user);
        return "Account successfully rejected";
    }

    @Override
    public String forgotPassword(String email) {
        return userRepository.findByEmail(email)
                .map(mailSender::sendEmail)
                .orElseThrow(() -> new UserNotFoundException(INVALID_EMAIL));
    }

    @Override
    public UserDtoResponse resetPassword(PasswordResetDto passwordResetDto) {
        Optional<PasswordResetToken> passwordResetToken = tokenRepository.findByToken(passwordResetDto.getUserToken());
        if (passwordResetToken.isEmpty()) {
            throw new InvalidResetTokenException(INVALID_RESET);
        }
        if (passwordResetToken.get().getExpireDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidResetTokenException(EXPIRED_RESET);
        }
        User user = passwordResetToken.get().getUserToken();
        user.setPassword(passwordResetDto.getPassword());
        userRepository.save(user);
        return UserMapper.mapToUserDtoResponse(user);
    }

    @Override
    public String deleteClientAccount(UUID clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if (user.getRole() == Role.ADMIN) {
            throw new InvalidAccountException(DELETE_ADMIN_ACCOUNT);
        }
        userRepository.delete(user);
        return "Client deleted successfully";
    }

    @Override
    public String deleteClientImage(UUID imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
        User user = userRepository.findById(image.getAlbum().getId().getUserId().getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if (user.getRole() == Role.ADMIN) {
            throw new InvalidAccountException(DELETE_ADMIN_ACCOUNT);
        }

        imageRepository.delete(image);
        return "Image deleted successfully";
    }

    private Map<String, String> createClaims(User user) {
        return Map.of(
                "user_id", user.getId().toString(),
                "user_role", user.getRole().toString()
        );
    }

    private User getInactiveClient(UUID clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (user.getRole() == Role.ADMIN) {
            throw new InvalidAccountException(ADMIN_EXCEPTION);
        }

        if (user.getActive()) {
            throw new InvalidAccountException(ALREADY_ACTIVATED_ACCOUNT);
        }

        return user;
    }
}
