package com.disi.social_platform_be.service.impl;

import com.disi.social_platform_be.dto.mapper.ImageMapper;
import com.disi.social_platform_be.dto.requests.BasicAlbumDto;
import com.disi.social_platform_be.dto.responses.AlbumDto;
import com.disi.social_platform_be.dto.responses.BasicImageDto;
import com.disi.social_platform_be.exception.AlbumNotFoundException;
import com.disi.social_platform_be.exception.DuplicateAlbumException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.Image;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.id.AlbumId;
import com.disi.social_platform_be.repository.IAlbumRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumService implements IAlbumService {

    private final IAlbumRepository albumRepository;
    private final IUserRepository userRepository;
    private final static String USER_NOT_FOUND = "User does not exists";
    private final static String ALBUM_CREATED = "Album was successful created";
    private final static String ALBUM_REMOVED = "Album was successful removed";
    private final static String DUPLICATE_ALBUM = "Album with this name for this user already exists";
    private final static String ALBUM_NOT_FOUND = "Album does not exists";

    @Override
    public String createAlbum(BasicAlbumDto basicAlbumDto) {
        Optional<User> user = userRepository.findById(UUID.fromString(basicAlbumDto.getUserId()));
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        AlbumId albumId = new AlbumId(user.get(), basicAlbumDto.getName());
        Optional<Album> foundAlbum = albumRepository.findById(albumId);
        if (foundAlbum.isPresent()) {
            throw new DuplicateAlbumException(DUPLICATE_ALBUM);
        }
        albumRepository.save(new Album(albumId));
        return ALBUM_CREATED;
    }

    @Override
    public String removeAlbum(BasicAlbumDto basicAlbumDto) {
        Optional<User> user = userRepository.findById(UUID.fromString(basicAlbumDto.getUserId()));
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        albumRepository.deleteById(new AlbumId(user.get(), basicAlbumDto.getName()));
        return ALBUM_REMOVED;
    }

    @Override
    public List<AlbumDto> getAllAlbumsByUser(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        List<AlbumDto> basicAlbumDtos = new ArrayList<>();
        for (Album album : user.get().getAlbums()) {
            basicAlbumDtos.add(new AlbumDto(album.getId().getUserId().getId().toString(), album.getId().getName(), mapImages(album)));
        }
        return basicAlbumDtos;
    }

    @Override
    public AlbumDto getAlbum(String userId, String albumName) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        Optional<Album> album = albumRepository.findById(new AlbumId(user.get(), albumName));
        if (album.isEmpty()) {
            throw new AlbumNotFoundException(ALBUM_NOT_FOUND);
        }
        return new AlbumDto(album.get().getId().getUserId().getId().toString(), album.get().getId().getName(), mapImages(album.get()));
    }

    private List<BasicImageDto> mapImages(Album album) {
        List<BasicImageDto> imageDtos = new ArrayList<>();
        for (Image image : album.getImages()) {
            imageDtos.add(ImageMapper.mapToBasicDto(image));
        }
        return imageDtos;
    }
}
