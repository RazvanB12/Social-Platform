package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.BasicDetailDto;
import com.disi.social_platform_be.dto.responses.DetailDto;
import com.disi.social_platform_be.dto.responses.ProfilePictureDto;
import org.springframework.web.multipart.MultipartFile;

public interface IDetailService {

    String updateDetail(String userId, BasicDetailDto detailDto);

    DetailDto getDetail(String userId);

    String updateProfilePicture(String userId, MultipartFile file);

    ProfilePictureDto getProfilePicture(String userId);

    String deleteDetail(String userId);

    String deleteProfilePicture(String userId);
}
