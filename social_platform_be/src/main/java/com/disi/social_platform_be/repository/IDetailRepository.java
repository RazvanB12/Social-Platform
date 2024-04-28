package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDetailRepository extends CrudRepository<Detail, UUID> {

    List<Detail> findAllByUserIn(List<User> users);

    Optional<Detail> findByUser(User user);
}
