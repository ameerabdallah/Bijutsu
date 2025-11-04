package com.ameerdev.model.media;

import lombok.Builder;

import java.util.Date;

@Builder(toBuilder = true)
public record ReleaseMetadata(
        int id,
        String metadataSourceId,
        String title,
        Date releaseDate,
        int index
) {
}
