package com.ameerdev.metadata_agent;

import com.ameerdev.jooq.enums.BookType;
import com.ameerdev.jooq.tables.pojos.Release;
import com.ameerdev.jooq.tables.pojos.Series;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface MetadataAgent {
    @Nonnull
    BookType metadataType();

    /**
     * @param name expects an already parsed name
     * @return Metadata source's ID. If multiple results are found, return the result that best matches.
     */
    Optional<String> searchByName(String name);

    Optional<Series> fetchSeriesMetadata(String metadataSeriesId);

    default Optional<Release> fetchReleaseMetadata(String metadataSeriesId, int index) {
        Release release = new Release();
        return Optional.of(release.setTitle("Chapter " + index).setIndex(index));
    }
}
