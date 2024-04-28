package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AlbumDto {

    private String userId;
    private String name;
    private List<BasicImageDto> images;
}
