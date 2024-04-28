package com.disi.social_platform_be.service;

import com.disi.social_platform_be.dto.requests.BasicAlbumDto;
import com.disi.social_platform_be.dto.responses.AlbumDto;

import java.util.List;

public interface IAlbumService {

    String createAlbum(BasicAlbumDto basicAlbumDto);

    String removeAlbum(BasicAlbumDto basicAlbumDto);

    List<AlbumDto> getAllAlbumsByUser(String userId);

    AlbumDto getAlbum(String userId, String albumName);
}
