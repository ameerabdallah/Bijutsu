package com.ameerdev.models;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class SeriesIdentifier {
    @Nullable
    private String metadataSourceId;
    @Nullable
    private String title;
    @Nullable
    private Integer year;

    public Optional<String> getMetadataSourceId() {
        return Optional.ofNullable(metadataSourceId);
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<Integer> getYear() {
        return Optional.ofNullable(year);
    }
}