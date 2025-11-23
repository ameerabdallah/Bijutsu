package com.ameerdev.models;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {
    private long id;
    private long libraryId;
    private String path;
    private String title;
    private String author;
    private String description;
    private Integer releaseYear;
    private String metadataSourceId;

    public Optional<String> getMetadataSourceId() {
        return Optional.ofNullable(metadataSourceId);
    }
}
