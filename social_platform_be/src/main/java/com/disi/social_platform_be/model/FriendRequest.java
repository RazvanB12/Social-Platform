package com.disi.social_platform_be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @NotNull
    private Boolean approved;

    @ManyToOne
    @JoinColumn(name = "fromUser", nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "toUser", nullable = false)
    private User toUser;

    public FriendRequest(User fromUser, User toUser) {
        this.approved = false;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
