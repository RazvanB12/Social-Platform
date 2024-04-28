package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDetailDto {

    private byte[] content;
    private String type;
    private Long uploadDate;
}
