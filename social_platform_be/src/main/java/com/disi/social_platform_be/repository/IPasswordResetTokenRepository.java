package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.PasswordResetToken;
import com.disi.social_platform_be.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPasswordResetTokenRepository extends CrudRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUserToken(User userToken);
}
