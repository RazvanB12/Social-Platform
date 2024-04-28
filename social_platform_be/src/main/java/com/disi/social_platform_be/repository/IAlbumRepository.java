package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.Album;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.id.AlbumId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAlbumRepository extends CrudRepository<Album, AlbumId> {

    @Query("SELECT a FROM Album a WHERE a.id.userId IN :users")
    List<Album> findAllByUsers(@Param("users") List<User> users);
}
