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
public class Image implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @NotNull
    @Column(name = "content", unique = false, nullable = false, columnDefinition = "BYTEA")
    private byte[] content;

    @NotNull
    private String type;

    @NotNull
    private Long uploadDate;

    @NotNull
    private Boolean publicImage = false;

    @NotNull
    private Boolean hasBlockRequest = false;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "album_userId", referencedColumnName = "userId"),
            @JoinColumn(name = "album_name", referencedColumnName = "name")
    })
    private Album album;

    public Image(byte[] content, String type, Long uploadDate, Album album) {
        this.content = content;
        this.type = type;
        this.uploadDate = uploadDate;
        this.album = album;
    }
}