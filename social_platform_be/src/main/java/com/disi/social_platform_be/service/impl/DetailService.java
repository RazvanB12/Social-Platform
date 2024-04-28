package com.disi.social_platform_be.service.impl;

import com.disi.social_platform_be.dto.mapper.DetailMapper;
import com.disi.social_platform_be.dto.requests.BasicDetailDto;
import com.disi.social_platform_be.dto.responses.DetailDto;
import com.disi.social_platform_be.dto.responses.ProfilePictureDto;
import com.disi.social_platform_be.exception.ProfilePictureScalingException;
import com.disi.social_platform_be.exception.UserNotFoundException;
import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.repository.IDetailRepository;
import com.disi.social_platform_be.repository.IUserRepository;
import com.disi.social_platform_be.service.IDetailService;
import com.disi.social_platform_be.util.PictureScaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DetailService implements IDetailService {

    private final IDetailRepository detailRepository;
    private final IUserRepository userRepository;
    private static final String INVALID_USER_ID = "Invalid user id";
    private static final String SUCCESS = "Details successful updated";
    private static final String PROFILE_PICTURE_ERROR = "Profile picture can't be scaled";

    @Override
    public String updateDetail(String userId, BasicDetailDto detailDto) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> {
                    Detail detail = user.getDetail();
                    updateBasicDetails(detail, detailDto);
                    detailRepository.save(detail);
                    return SUCCESS;
                })
                .orElseThrow(() -> new UserNotFoundException(INVALID_USER_ID));
    }

    @Override
    public DetailDto getDetail(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> DetailMapper.mapToDetailDto(user.getDetail()))
                .orElseThrow(() -> new UserNotFoundException(INVALID_USER_ID));
    }

    @Override
    public String updateProfilePicture(String userId, MultipartFile file) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> {
                    Detail detail = user.getDetail();
                    try {
                        updateProfilePicture(detail, file);
                    } catch (IOException e) {
                        throw new ProfilePictureScalingException(PROFILE_PICTURE_ERROR);
                    }
                    detailRepository.save(detail);
                    return SUCCESS;
                })
                .orElseThrow(() -> new UserNotFoundException(INVALID_USER_ID));
    }

    @Override
    public ProfilePictureDto getProfilePicture(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> DetailMapper.mapToProfilePictureDto(user.getDetail()))
                .orElseThrow(() -> new UserNotFoundException(INVALID_USER_ID));
    }

    @Override
    public String deleteDetail(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> {
                    Detail detail = user.getDetail();
                    deleteBasicDetails(detail);
                    detailRepository.save(detail);
                    return SUCCESS;
                })
                .orElseThrow(() -> new UserNotFoundException(INVALID_USER_ID));
    }

    @Override
    public String deleteProfilePicture(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> {
                    Detail detail = user.getDetail();
                    removeProfilePicture(detail);
                    detailRepository.save(detail);
                    return SUCCESS;
                })
                .orElseThrow(() -> new UserNotFoundException(INVALID_USER_ID));
    }

    private void updateBasicDetails(Detail detail, BasicDetailDto detailDto) {
        detail.setDescription(detailDto.getDescription());
        detail.setAddress(detailDto.getAddress());
        detail.setHobbies(detailDto.getHobbies());
        detail.setPublicDetails(detailDto.getPublicDetails());
    }

    private void updateProfilePicture(Detail detail, MultipartFile file) throws IOException {
        detail.setProfilePictureContent(PictureScaller.scale(file.getBytes()));
        detail.setProfilePictureExtension("image/jpg");
    }

    private void deleteBasicDetails(Detail detail) {
        detail.setDescription(null);
        detail.setAddress(null);
        detail.setHobbies(null);
        detail.setPublicDetails(null);
    }

    private void removeProfilePicture(Detail detail) {
        detail.setProfilePictureContent(null);
        detail.setProfilePictureExtension(null);
    }
}
