package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.UpdateImageDto;
import com.disi.social_platform_be.exception.ImageNotFoundException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.FriendRequest;
import com.disi.social_platform_be.model.Image;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.id.AlbumId;
import com.disi.social_platform_be.repository.IAlbumRepository;
import com.disi.social_platform_be.repository.IFriendRequestRepository;
import com.disi.social_platform_be.repository.IImageRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.impl.ImageService;
import com.disi.social_platform_be.util.TestDataBuilder;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private IImageRepository imageRepository;
    @Mock
    private IAlbumRepository albumRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IFriendRequestRepository friendRequestRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    public void givenValidImageId_whenDelete_thenReturnSuccessfullyMessage() {
        UUID imageId = UUID.randomUUID();

        doNothing().when(imageRepository).deleteById(imageId);
        var response = imageService.deleteImage(imageId.toString());

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Image was successful deleted", response);
    }

    @Test
    public void givenValidImageId_whenUpdateImagePrivacy_thenReturnSuccessfullyMessage() {
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        UpdateImageDto imageDto = new UpdateImageDto(imageId.toString(), "public");

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        when(imageRepository.save(image)).thenReturn(null);
        var response = imageService.updateImagePrivacy(imageDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Image was successful saved", response);
    }

    @Test
    public void givenInvalidImageId_whenUpdateImagePrivacy_thenThrowException() {
        UUID imageId = UUID.randomUUID();
        UpdateImageDto imageDto = new UpdateImageDto(imageId.toString(), "public");

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.updateImagePrivacy(imageDto));
    }

    @Test
    public void givenValidImageId_whenGetImageById_thenReturnImageDto() {
        UUID imageId = UUID.randomUUID();
        User user = new User();
        Album album = new Album(new AlbumId(user, "albumName"), new HashSet<>());
        Image image = new Image(imageId, new byte[1024], "JPG", 1234L, true, false, album);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        var response = imageService.getImageById(imageId.toString());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getPublicImage(), image.getPublicImage());
        Assertions.assertEquals(response.getType(), image.getType());
        Assertions.assertEquals(response.getUploadDate(), image.getUploadDate());
        Assertions.assertEquals(response.getContent(), image.getContent());
        Assertions.assertEquals(response.getAlbumName(), image.getAlbum().getId().getName());
    }

    @Test
    public void givenInvalidImageId_whenGetImageById_thenThrowException() {
        UUID imageId = UUID.randomUUID();

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.getImageById(imageId.toString()));
    }

    @Test
    public void givenValidImageId_whenReportImage_thenReturnSuccessfullyMessage() {
        UUID imageId = UUID.randomUUID();
        User user = new User();
        Album album = new Album(new AlbumId(user, "albumName"), new HashSet<>());
        Image image = new Image(imageId, new byte[1024], "JPG", 1234L, true, false, album);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        var response = imageService.reportImage(imageId.toString());

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Image was successful reported", response);
    }

    @Test
    public void givenInvalidImageId_whenReportImage_thenThrowException() {
        UUID imageId = UUID.randomUUID();

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.reportImage(imageId.toString()));
    }

    @Test
    public void givenValidImageId_whenBlockImage_thenReturnSuccessfullyMessage() {
        UUID imageId = UUID.randomUUID();

        doNothing().when(imageRepository).deleteById(imageId);
        var response = imageService.deleteImage(imageId.toString());

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Image was successful deleted", response);
    }

    @Test
    public void givenValidImageId_whenRejectReportedImage_thenReturnSuccessfullyMessage() {
        UUID imageId = UUID.randomUUID();
        User user = new User();
        Album album = new Album(new AlbumId(user, "albumName"), new HashSet<>());
        Image image = new Image(imageId, new byte[1024], "JPG", 1234L, true, false, album);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        var response = imageService.rejectReportedImage(imageId.toString());

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Image was successful unreported", response);
    }

    @Test
    public void givenInvalidImageId_whenRejectReportImage_thenThrowException() {
        UUID imageId = UUID.randomUUID();

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.rejectReportedImage(imageId.toString()));
    }

    @Test
    public void givenReportedImages_whenGetReportedImages_thenReturnBasicImageDtoList() {
        Album album = new Album(new AlbumId(new User(), "albumName"), new HashSet<>());
        List<Image> images = List.of(
            new Image(UUID.randomUUID(), new byte[1024], "JPG", 1234L, true, true, album)
        );

        when(imageRepository.findAllByHasBlockRequest(true)).thenReturn(images);
        var response = imageService.getReportedImages();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(images.get(0).getId().toString(), response.get(0).getImageId());
        Assertions.assertEquals(images.get(0).getType(), response.get(0).getType());
        Assertions.assertEquals(images.get(0).getUploadDate(), response.get(0).getUploadDate());
    }

    @Test
    public void givenExistingUsers_whenGetNewsFeed_thenReturnNewsFeedImageList() {
        UUID currentUserId = UUID.randomUUID();
        User currentUser = TestDataBuilder.createUser(currentUserId);
        UUID friendId = UUID.randomUUID();
        User friendUser = TestDataBuilder.createUser(friendId);
        UUID anotherFriendId = UUID.randomUUID();
        User anotherFriendUser = TestDataBuilder.createUser(anotherFriendId);
        List<FriendRequest> friendRequests = List.of(
                new FriendRequest(UUID.randomUUID(), true, currentUser, friendUser),
                new FriendRequest(UUID.randomUUID(), true, currentUser, anotherFriendUser)
        );
        List<Album> albums = List.of(
                new Album(new AlbumId(friendUser, "album1"), new HashSet<>()),
                new Album(new AlbumId(anotherFriendUser, "album2"), new HashSet<>())
        );
        List<Image> images = new ArrayList<>();
        images.add(new Image(UUID.randomUUID(), new byte[1024], "png", 1L, true, false, albums.get(0)));
        images.add(new Image(UUID.randomUUID(), new byte[1024], "png", 3L, true, false, albums.get(0)));
        images.add(new Image(UUID.randomUUID(), new byte[1024], "png", 4L, true, false, albums.get(1)));
        images.add(new Image(UUID.randomUUID(), new byte[1024], "png", 2L, true, false, albums.get(1)));

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(friendRequestRepository.findAllByFromUserAndApprovedOrToUserAndApproved(currentUser, true, currentUser, true))
                .thenReturn(friendRequests);
        when(albumRepository.findAllByUsers(any())).thenReturn(albums);
        when(imageRepository.findAllByAlbumIn(albums)).thenReturn(images);

        var response = imageService.getNewsFeed(currentUserId.toString());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(4, response.size());
        Assertions.assertEquals(4L, response.get(0).getImage().getUploadDate());
        Assertions.assertEquals(3L, response.get(1).getImage().getUploadDate());
        Assertions.assertEquals(2L, response.get(2).getImage().getUploadDate());
        Assertions.assertEquals(1L, response.get(3).getImage().getUploadDate());
    }

    @Test
    public void givenNotExistingUser_whenGetNewsFeed_thenReturnNewsFeedImageList() {
        UUID currentUserId = UUID.randomUUID();

        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> imageService.getNewsFeed(currentUserId.toString()));
    }
}
