package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.BasicAlbumDto;
import com.disi.social_platform_be.exception.AlbumNotFoundException;
import com.disi.social_platform_be.exception.DuplicateAlbumException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.id.AlbumId;
import com.disi.social_platform_be.repository.IAlbumRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.impl.AlbumService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private IAlbumRepository albumRepository;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private AlbumService albumService;

    @Test
    public void givenValidAlbum_whenCreate_ThenReturnSuccessfullyMessage() {
        BasicAlbumDto albumDto = new BasicAlbumDto(UUID.randomUUID().toString(), "albumName");
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(albumRepository.findById(any())).thenReturn(Optional.empty());
        when(albumRepository.save(any())).thenReturn(null);

        var response = albumService.createAlbum(albumDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Album was successful created", response);
    }

    @Test
    public void givenExistingAlbum_whenCreate_ThenThrowException() {
        BasicAlbumDto albumDto = new BasicAlbumDto(UUID.randomUUID().toString(), "albumName");
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(albumRepository.findById(any())).thenReturn(Optional.of(new Album()));

        Assertions.assertThrows(DuplicateAlbumException.class, () -> albumService.createAlbum(albumDto));
    }

    @Test
    public void givenNotExistingUser_whenCreate_ThenThrowException() {
        BasicAlbumDto albumDto = new BasicAlbumDto(UUID.randomUUID().toString(), "albumName");
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> albumService.createAlbum(albumDto));
    }

    @Test
    public void givenExistingAlbum_whenRemove_ThenReturnSuccessfullyMessage() {
        BasicAlbumDto albumDto = new BasicAlbumDto(UUID.randomUUID().toString(), "albumName");
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        doNothing().when(albumRepository).deleteById(any());

        var response = albumService.removeAlbum(albumDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Album was successful removed", response);
    }

    @Test
    public void givenNotExistingUser_whenRemove_ThenThrowException() {
        BasicAlbumDto albumDto = new BasicAlbumDto(UUID.randomUUID().toString(), "albumName");
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> albumService.removeAlbum(albumDto));
    }

    @Test
    public void givenExistingUser_whenGetAllAlbumsByUser_returnAllUserAlbums() {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(UUID.fromString(userId));
        List<Album> albums = List.of(
                new Album(new AlbumId(user, "Album1"), new HashSet<>()),
                new Album(new AlbumId(user, "Album2"), new HashSet<>()),
                new Album(new AlbumId(user, "Album3"), new HashSet<>()),
                new Album(new AlbumId(user, "Album4"), new HashSet<>())
        );
        user.setAlbums(new HashSet<>(albums));

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        var response = albumService.getAllAlbumsByUser(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(4, response.size());
    }

    @Test
    public void givenNotExistingUser_whenGetAllAlbumsByUser_throwException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> albumService.getAllAlbumsByUser(userId));
    }

    @Test
    public void givenExistingAlbumAndUser_whenGetAlbum_ReturnAlbumDto() {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(UUID.fromString(userId));
        String albumName = "albumName";
        Album album = new Album();
        album.setId(new AlbumId(user, albumName));
        album.setImages(new HashSet<>());

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(albumRepository.findById(any())).thenReturn(Optional.of(album));
        var response = albumService.getAlbum(userId, albumName);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getUserId(), userId);
        Assertions.assertEquals(response.getName(), albumName);
        Assertions.assertEquals(response.getImages().size(), 0);
    }

    @Test
    public void givenNotExistingUser_whenGetAlbum_throwException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> albumService.getAlbum(userId, "albumName"));
    }

    @Test
    public void givenNotExistingAlbum_whenGetAlbum_throwException() {
        String userId = UUID.randomUUID().toString();

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(new User()));
        when(albumRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(AlbumNotFoundException.class, () -> albumService.getAlbum(userId, "albumName"));
    }
}
