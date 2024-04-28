package com.disi.social_platform_be.model;

import com.disi.social_platform_be.model.id.AlbumId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Album implements Serializable {

    @EmbeddedId
    private AlbumId id;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Image> images;

    public Album(AlbumId id) {
        this.id = id;
    }
}
