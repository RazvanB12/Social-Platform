package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.LoginDto;
import com.disi.social_platform_be.dto.requests.PasswordResetDto;
import com.disi.social_platform_be.dto.requests.RegisterDto;
import com.disi.social_platform_be.exception.InactiveAccountException;
import com.disi.social_platform_be.exception.InvalidAccountException;
import com.disi.social_platform_be.exception.InvalidResetTokenException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.Image;
import com.disi.social_platform_be.model.PasswordResetToken;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.Role;
import com.disi.social_platform_be.model.id.AlbumId;
import com.disi.social_platform_be.repository.IDetailRepository;
import com.disi.social_platform_be.repository.IImageRepository;
import com.disi.social_platform_be.repository.IPasswordResetTokenRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.impl.UserService;
import com.disi.social_platform_be.util.AuthenticationService;
import com.disi.social_platform_be.util.MailSender;
import com.disi.social_platform_be.util.TestDataBuilder;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IDetailRepository detailRepository;
    @Mock
    private IPasswordResetTokenRepository tokenRepository;
    @Mock
    private IImageRepository imageRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private MailSender mailSender;

    @InjectMocks
    private UserService userService;

    @Test
    public void givenValidCredentials_whenLogin_thenReturnLoginResponseDto() {
        LoginDto loginDto = new LoginDto("email", "pass");
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        String token = "token";

        when(userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())).thenReturn(Optional.of(currentUser));
        when(authenticationService.generateToken(any())).thenReturn(token);
        var response = userService.login(loginDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(token, response.getToken());
        Assertions.assertEquals(currentUserId, response.getUserId());
        Assertions.assertEquals(Role.CLIENT, response.getRole());
    }

    @Test
    public void givenInvalidCredentials_whenLogin_thenThrowException() {
        LoginDto loginDto = new LoginDto("email", "pass");

        when(userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.login(loginDto));
    }

    @Test
    public void givenValidCredentialsForNotActivatedAccount_whenLogin_thenThrowException() {
        LoginDto loginDto = new LoginDto("email", "pass");
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        currentUser.setActive(false);

        when(userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())).thenReturn(Optional.of(currentUser));

        Assertions.assertThrows(InactiveAccountException.class, () -> userService.login(loginDto));
    }

    @Test
    public void givenNotActivatedAccounts_whenGetPendingAccounts_thenReturnUserDtoResponseList() {
        List<User> pendingUsers = List.of(
                createInactiveUser(),
                createInactiveUser(),
                createInactiveUser(),
                createInactiveUser()
        );

        when(userRepository.findAllByRoleAndActive(Role.CLIENT, false)).thenReturn(pendingUsers);
        var response = userService.getAllPendingUsers();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(4, response.size());
    }

    @Test
    public void givenAccounts_whenGetClients_thenReturnClientDtoResponseList() {
        List<User> clients = List.of(
                TestDataBuilder.createUser(UUID.randomUUID()),
                TestDataBuilder.createUser(UUID.randomUUID()),
                TestDataBuilder.createUser(UUID.randomUUID()),
                TestDataBuilder.createUser(UUID.randomUUID())
        );

        when(userRepository.findAllByRole(Role.CLIENT)).thenReturn(clients);
        var response = userService.getAllClients();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(4, response.size());
        response.forEach(u -> Assertions.assertEquals(true, u.getActive()));
    }

    @Test
    public void givenValidRegisterDto_whenRegister_thenReturnUserDtoResponse() {
        RegisterDto registerDto = new RegisterDto("firstName", "lastName", "email", "password", true);

        when(detailRepository.save(any())).thenReturn(null);
        when(userRepository.save(any())).thenReturn(new User());
        var response = userService.register(registerDto);

        Assertions.assertNotNull(response);
    }

    @Test
    public void givenInactiveAccountId_whenActivateClientAccount_thenReturnSuccessfullyMessage() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(null);
        var response = userService.activateClientAccount(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Account successfully activated", response);
    }

    @Test
    public void givenActiveAccountId_whenActivateClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidAccountException.class, () -> userService.activateClientAccount(userId));
    }

    @Test
    public void givenAdminAccountId_whenActivateClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setRole(Role.ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidAccountException.class, () -> userService.activateClientAccount(userId));
    }

    @Test
    public void givenNotExistingUserId_whenActivateClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.activateClientAccount(userId));
    }
    //
    @Test
    public void givenInactiveAccountId_whenRejectClientAccount_thenReturnSuccessfullyMessage() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any());
        var response = userService.rejectClientAccount(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Account successfully rejected", response);
    }

    @Test
    public void givenActiveAccountId_whenRejectClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidAccountException.class, () -> userService.rejectClientAccount(userId));
    }

    @Test
    public void givenAdminAccountId_whenRejectClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setRole(Role.ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidAccountException.class, () -> userService.rejectClientAccount(userId));
    }

    @Test
    public void givenNotExistingUserId_whenRejectClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.rejectClientAccount(userId));
    }

    @Test
    public void givenValidAccount_whenForgotPassword_thenReturnSuccessfullyMessage() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mailSender.sendEmail(user)).thenReturn("OK");
        var response = userService.forgotPassword(user.getEmail());

        Assertions.assertNotNull(response);
    }

    @Test
    public void givenNotExistingAccount_whenForgotPassword_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.forgotPassword(user.getEmail()));
    }

    @Test
    public void givenValidPasswordResetDto_whenResetPassword_thenReturnUserDtoResponse() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        PasswordResetDto resetDto = new PasswordResetDto();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setExpireDateTime(LocalDateTime.MAX);
        passwordResetToken.setUserToken(user);

        when(tokenRepository.findByToken(resetDto.getUserToken())).thenReturn(Optional.of(passwordResetToken));
        when(userRepository.save(user)).thenReturn(null);
        var response = userService.resetPassword(resetDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(userId, response.getId());
        Assertions.assertEquals(Role.CLIENT, response.getRole());
    }

    @Test
    public void givenInvalidPasswordResetToken_whenResetPassword_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        PasswordResetDto resetDto = new PasswordResetDto();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setExpireDateTime(LocalDateTime.MAX);
        passwordResetToken.setUserToken(user);

        when(tokenRepository.findByToken(resetDto.getUserToken())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidResetTokenException.class, () -> userService.resetPassword(resetDto));
    }

    @Test
    public void givenExpiredPasswordResetToken_whenResetPassword_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        PasswordResetDto resetDto = new PasswordResetDto();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setExpireDateTime(LocalDateTime.MIN);
        passwordResetToken.setUserToken(user);

        when(tokenRepository.findByToken(resetDto.getUserToken())).thenReturn(Optional.of(passwordResetToken));

        Assertions.assertThrows(InvalidResetTokenException.class, () -> userService.resetPassword(resetDto));
    }

    @Test
    public void givenValidClientId_whenDeleteClientAccount_thenReturnSuccessfullyMessage() {
        UUID clientId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(clientId);

        when(userRepository.findById(clientId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        var response = userService.deleteClientAccount(clientId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Client deleted successfully", response);
    }

    @Test
    public void givenNotExistingClientId_whenDeleteClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteClientAccount(userId));
    }

    @Test
    public void givenAdminId_whenDeleteClientAccount_thenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setRole(Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidAccountException.class, () -> userService.deleteClientAccount(userId));
    }

    @Test
    public void givenValidAccount_whenDeleteClientImage_ThenReturnSuccessfullyMessage() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        image.setAlbum(new Album(new AlbumId(user, "")));

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(imageRepository).delete(image);
        var response = userService.deleteClientImage(imageId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Image deleted successfully", response);
    }

    @Test
    public void givenAdminAccount_whenDeleteClientImage_ThenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setRole(Role.ADMIN);
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        image.setAlbum(new Album(new AlbumId(user, "")));

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidAccountException.class, () -> userService.deleteClientImage(imageId));
    }

    @Test
    public void givenNotExistingClient_whenDeleteClientImage_ThenThrowException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.createUser(userId);
        user.setRole(Role.ADMIN);
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        image.setAlbum(new Album(new AlbumId(user, "")));

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteClientImage(imageId));
    }

    private User createInactiveUser() {
        User user = TestDataBuilder.createUser(UUID.randomUUID());
        user.setActive(false);
        return user;
    }
}
