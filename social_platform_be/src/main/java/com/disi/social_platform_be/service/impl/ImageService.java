package com.disi.social_platform_be.service.impl;

import com.disi.social_platform_be.dto.mapper.DetailMapper;
import com.disi.social_platform_be.dto.mapper.ImageMapper;
import com.disi.social_platform_be.dto.mapper.UserMapper;
import com.disi.social_platform_be.dto.requests.UpdateImageDto;
import com.disi.social_platform_be.dto.responses.*;
import com.disi.social_platform_be.exception.AlbumNotFoundException;
import com.disi.social_platform_be.exception.ImageNotFoundException;
import com.disi.social_platform_be.exception.ProfilePictureScalingException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.Image;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.id.AlbumId;
import com.disi.social_platform_be.repository.IAlbumRepository;
import com.disi.social_platform_be.repository.IFriendRequestRepository;
import com.disi.social_platform_be.repository.IImageRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.IImageService;
import com.disi.social_platform_be.util.ImageSorter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final IImageRepository imageRepository;
    private final IAlbumRepository albumRepository;
    private final IUserRepository userRepository;
    private final IFriendRequestRepository friendRequestRepository;

    private static final String INVALID_USER_ID = "Invalid user id";
    private static final String INVALID_ALBUM_ID = "Invalid album id";
    private static final String IMAGE_SAVED = "Image was successful saved";
    private static final String IMAGE_DELETED = "Image was successful deleted";
    private static final String IMAGE_REPORTED = "Image was successful reported";
    private static final String IMAGE_UNREPORTED = "Image was successful unreported";
    private static final String IMAGE_NOT_FOUND = "Image not found";
    private static final String FAIL = "Fail to save the image";

    @Override
    public String createImage(MultipartFile file, String userId, String albumName) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new UserNotFoundException(INVALID_USER_ID);
        }

        Optional<Album> album = albumRepository.findById(new AlbumId(user.get(), albumName));
        if (album.isEmpty()) {
            throw new AlbumNotFoundException(INVALID_ALBUM_ID);
        }

        try {
            imageRepository.save(ImageMapper.mapToImage(file, album.get()));
        } catch (IOException e) {
            throw new ProfilePictureScalingException(FAIL);
        }

        return IMAGE_SAVED;
    }

    @Override
    public String deleteImage(String imageId) {
        imageRepository.deleteById(UUID.fromString(imageId));
        return IMAGE_DELETED;
    }

    @Override
    public String updateImagePrivacy(UpdateImageDto updateImageDto) {
        return imageRepository.findById(UUID.fromString(updateImageDto.getImageId()))
                .map(image -> {
                    image.setPublicImage(Boolean.valueOf(updateImageDto.getPublicImage()));
                    imageRepository.save(image);
                    return IMAGE_SAVED;
                })
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    @Override
    public ImageDto getImageById(String imageId) {
        return imageRepository.findById(UUID.fromString(imageId))
                .map(ImageMapper::mapToDto)
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    @Override
    public String reportImage(String imageId) {
        return imageRepository.findById(UUID.fromString(imageId))
                .map(image -> {
                    image.setHasBlockRequest(true);
                    imageRepository.save(image);
                    return IMAGE_REPORTED;
                })
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    @Override
    public String blockImage(String imageId) {
        imageRepository.deleteById(UUID.fromString(imageId));
        return IMAGE_DELETED;
    }

    @Override
    public String rejectReportedImage(String imageId) {
        return imageRepository.findById(UUID.fromString(imageId))
                .map(image -> {
                    image.setHasBlockRequest(false);
                    imageRepository.save(image);
                    return IMAGE_UNREPORTED;
                })
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    @Override
    public List<BasicImageDto> getReportedImages() {
        return imageRepository.findAllByHasBlockRequest(true)
                .stream()
                .map(ImageMapper::mapToBasicDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsFeedImage> getNewsFeed(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new UserNotFoundException(INVALID_USER_ID);
        }

        List<User> friends = friendRequestRepository.findAllByFromUserAndApprovedOrToUserAndApproved(user.get(), true, user.get(), true)
                .stream()
                .map(friendRequest -> friendRequest.getToUser() == user.get() ? friendRequest.getFromUser() : friendRequest.getToUser())
                .toList();

        List<Album> albums = albumRepository.findAllByUsers(friends);

        List<Image> images = imageRepository.findAllByAlbumIn(albums);
        ImageSorter.sortImagesByTimestampDescending(images);

        return images.stream().map(
                image -> {
                    Album imageAlbum = image.getAlbum();
                    User imageUser = imageAlbum.getId().getUserId();
                    Detail imageUserDetail = imageUser.getDetail();
                    UserDetailDto userDetailDto = UserMapper.mapToUserDetailDto(imageUser);
                    ImageDetailDto imageDetailDto = ImageMapper.mapToImageDetailDto(image);
                    ProfilePictureDto profilePictureDto = DetailMapper.mapToProfilePictureDto(imageUserDetail);
                    return new NewsFeedImage(userDetailDto, profilePictureDto, imageDetailDto);
                }).toList();
    }
}
