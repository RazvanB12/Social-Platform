package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.BasicDetailDto;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.repository.IDetailRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.impl.DetailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DetailServiceTest {

    @Mock
    private IDetailRepository detailRepository;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private DetailService detailService;

    @Test
    public void givenExistingUser_whenUpdateDetail_ThenReturnSuccessfullyMessage() {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setDetail(new Detail());
        user.setId(UUID.fromString(userId));
        BasicDetailDto detailDto = new BasicDetailDto("desc", "add", "hobbies", true);

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(detailRepository.save(any())).thenReturn(null);
        var response = detailService.updateDetail(userId, detailDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Details successful updated", response);
    }

    @Test
    public void givenNotExistingUser_whenUpdateDetail_ThenThrowException() {
        String userId = UUID.randomUUID().toString();
        BasicDetailDto detailDto = new BasicDetailDto("desc", "add", "hobbies", true);

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> detailService.updateDetail(userId, detailDto));
    }

    @Test
    public void givenExistingUser_whenGetDetail_ThenReturnDetailDto() {
        String userId = UUID.randomUUID().toString();
        BasicDetailDto detailDto = new BasicDetailDto("desc", "add", "hobbies", true);
        Detail details = new Detail();
        details.setDescription(detailDto.getDescription());
        details.setHobbies(detailDto.getHobbies());
        details.setAddress(detailDto.getAddress());
        details.setPublicDetails(detailDto.getPublicDetails());
        User user = new User();
        user.setDetail(details);
        user.setId(UUID.fromString(userId));

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        var response = detailService.getDetail(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(detailDto.getAddress(), response.getAddress());
        Assertions.assertEquals(detailDto.getHobbies(), response.getHobbies());
        Assertions.assertEquals(detailDto.getDescription(), response.getDescription());
        Assertions.assertEquals(detailDto.getPublicDetails(), response.getPublicDetails());
    }

    @Test
    public void givenNotExistingUser_whenGetDetail_ThenThrowException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> detailService.getDetail(userId));
    }

    @Test
    public void givenExistingUser_whenGetProfilePicture_ThenReturnProfilePictureDto() {
        String userId = UUID.randomUUID().toString();
        byte[] content = new byte[1024];
        BasicDetailDto detailDto = new BasicDetailDto("desc", "add", "hobbies", true);
        Detail details = new Detail();
        details.setDescription(detailDto.getDescription());
        details.setHobbies(detailDto.getHobbies());
        details.setAddress(detailDto.getAddress());
        details.setPublicDetails(detailDto.getPublicDetails());
        details.setProfilePictureExtension("png");
        details.setProfilePictureContent(content);
        User user = new User();
        user.setDetail(details);
        user.setId(UUID.fromString(userId));

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        var response = detailService.getProfilePicture(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("png", response.getExtension());
        Assertions.assertEquals(content, response.getContent());
    }

    @Test
    public void givenNotExistingUser_whenGetProfilePicture_ThenThrowException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> detailService.getProfilePicture(userId));
    }

    @Test
    public void givenExistingUser_whenDeleteDetail_ThenReturnSuccessfullyMessage() {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setDetail(new Detail());
        user.setId(UUID.fromString(userId));
        BasicDetailDto detailDto = new BasicDetailDto("desc", "add", "hobbies", true);

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(detailRepository.save(any())).thenReturn(null);
        var response = detailService.updateDetail(userId, detailDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Details successful updated", response);
    }

    @Test
    public void givenNotExistingUser_whenDeleteDetail_ThenThrowException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> detailService.deleteDetail(userId));
    }

    @Test
    public void givenExistingUser_whenDeleteProfilePicture_ThenReturnSuccessfullyMessage() {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setDetail(new Detail());
        user.setId(UUID.fromString(userId));

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(detailRepository.save(any())).thenReturn(null);
        var response = detailService.deleteProfilePicture(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Details successful updated", response);
    }

    @Test
    public void givenNotExistingUser_whenDeleteProfilePicture_ThenThrowException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> detailService.deleteProfilePicture(userId));
    }
}
