package com.ameerdev.models;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class SeriesIdentifier {
    @Nullable
    private String metadataSourceId;
    @Nullable
    private String title;
    @Nullable
    private Integer year;
}