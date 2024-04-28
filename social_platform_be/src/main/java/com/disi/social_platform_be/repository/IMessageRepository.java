package com.disi.social_platform_be.repository;

import com.disi.social_platform_be.model.Message;
import com.disi.social_platform_be.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface IMessageRepository extends CrudRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :currentUser AND m.receiver = :toUser) " +
            "OR (m.sender = :toUser AND m.receiver = :currentUser) " +
            "ORDER BY m.date")
    List<Message> getAllMessagesForUser(@Param("currentUser") User currentUser, @Param("toUser") User toUser);
}
