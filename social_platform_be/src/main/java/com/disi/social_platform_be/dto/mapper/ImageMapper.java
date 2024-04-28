package com.disi.social_platform_be.dto.mapper;

import com.disi.social_platform_be.dto.responses.BasicImageDto;
import com.disi.social_platform_be.dto.responses.ImageDetailDto;
import com.disi.social_platform_be.dto.responses.ImageDto;
import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.Image;
import com.disi.social_platform_be.util.PictureScaller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

public class ImageMapper {

    public static Image mapToImage(MultipartFile file, Album album) throws IOException {
        return new Image(PictureScaller.scale(file.getBytes()), "image/jpg", Instant.now().toEpochMilli(), album);
    }

    public static ImageDto mapToDto(Image image) {
        return new ImageDto(image.getContent(), image.getType(), image.getUploadDate(), image.getPublicImage(),
                image.getHasBlockRequest(), image.getAlbum().getId().getName());
    }

    public static BasicImageDto mapToBasicDto(Image image) {
        return new BasicImageDto(image.getId().toString(), image.getContent(), image.getType(), image.getUploadDate(), image.getPublicImage());
    }

    public static ImageDetailDto mapToImageDetailDto(Image image) {
        return new ImageDetailDto(image.getContent(), image.getType(), image.getUploadDate());
    }
}
