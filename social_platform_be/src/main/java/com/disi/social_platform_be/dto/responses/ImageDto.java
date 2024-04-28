package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {

    private byte[] content;
    private String type;
    private Long uploadDate;
    private Boolean publicImage;
    private Boolean hasBlockRequest;
    private String albumName;
}
