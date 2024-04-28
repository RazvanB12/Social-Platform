package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IImageRepository extends CrudRepository<Image, UUID> {

    List<Image> findAllByHasBlockRequest(Boolean hasBlockRequest);

    List<Image> findAllByAlbumIn(List<Album> albums);
}
