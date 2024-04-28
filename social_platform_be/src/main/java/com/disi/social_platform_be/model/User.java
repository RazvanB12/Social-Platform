package com.disi.social_platform_be.model;

import com.disi.social_platform_be.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String password;

    @NotNull
    private Role role;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean publicContent;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "detail", referencedColumnName = "id")
    private Detail detail;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Message> receivedMessages;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Message> sendMessages;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<FriendRequest> receivedRequests;

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<FriendRequest> sendRequests;

    @OneToMany(mappedBy = "id.userId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Album> albums;

    @OneToOne(mappedBy = "userToken", cascade = CascadeType.REMOVE)
    private PasswordResetToken passwordResetToken;

    public User(String email, String firstName, String lastName, String password, Role role, Boolean active, Boolean publicContent, Detail detail) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.active = active;
        this.publicContent = publicContent;
        this.detail = detail;
    }
}
