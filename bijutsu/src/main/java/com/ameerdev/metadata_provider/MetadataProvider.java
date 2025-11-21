package com.ameerdev.metadata_provider;

import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.Release;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface MetadataProvider {
    @Nonnull
    BookType metadataType();

    /**
     * Search for a metadata source's ID using the provided series identifier.
     *
     * @return Metadata source's ID. If multiple results are found, return the result that best matches.
     */
    Optional<String> search(SeriesIdentifier seriesIdentifier);

    Optional<Series> fetchSeriesMetadata(String metadataSeriesId);

    default Optional<Release> fetchReleaseMetadata(String metadataSeriesId, int index) {
        return Optional.of(
                Release.builder()
                        .title("Chapter " + index)
                        .index(index)
                        .build()
        );
    }
}
