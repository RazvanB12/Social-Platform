package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.enums.Role;
import com.disi.social_platform_be.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findAllByRoleAndActive(Role role, Boolean isActive);

    List<User> findAllByRole(Role role);

    Optional<User> findByEmail(String email);

    void deleteById(@NotNull UUID userId);
}
