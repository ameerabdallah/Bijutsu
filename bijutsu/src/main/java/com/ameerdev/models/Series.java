package com.ameerdev.models;

import lombok.*;

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
    private int releaseYear;
    private String metadataSourceId;
}
