package com.ameerdev.model.media;

import lombok.Builder;

import java.util.Date;

@Builder(toBuilder = true)
public record SeriesMetadata(
        int id,
        String metadataSourceId,
        String title,
        String description,
        Date releaseDate
) {
}
