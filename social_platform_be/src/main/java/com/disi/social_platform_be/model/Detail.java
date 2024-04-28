package com.disi.social_platform_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Detail implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String description;

    private String address;

    private String hobbies;

    @Column(name = "content", columnDefinition = "BYTEA")
    private byte[] profilePictureContent;

    private String profilePictureExtension;

    private Boolean publicDetails = false;

    @OneToOne(mappedBy = "detail", cascade = CascadeType.DETACH)
    private User user;
}
