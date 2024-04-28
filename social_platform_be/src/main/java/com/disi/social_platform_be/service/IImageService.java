package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.UpdateImageDto;
import com.disi.social_platform_be.dto.responses.BasicImageDto;
import com.disi.social_platform_be.dto.responses.ImageDto;
import com.disi.social_platform_be.dto.responses.NewsFeedImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    String createImage(MultipartFile file, String userId, String albumName);

    String deleteImage(String imageId);

    String updateImagePrivacy(UpdateImageDto updateImageDto);

    ImageDto getImageById(String imageId);

    String reportImage(String imageId);

    String blockImage(String imageId);

    String rejectReportedImage(String imageId);

    List<BasicImageDto> getReportedImages();

    List<NewsFeedImage> getNewsFeed(String userId);
}
