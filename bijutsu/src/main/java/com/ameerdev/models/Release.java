package com.ameerdev.models;

import com.ameerdev.models.enums.ReleaseType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Release {
    private long id;
    private int index;
    private String title;
    private String filePath;
    private LocalDate releaseDate;
    private ReleaseType releaseType;
    private String metadataSourceId;
}
