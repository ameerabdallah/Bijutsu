package com.ameerdev.metadata_provider;

import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.Release;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;

public interface MetadataProvider {
    @Nonnull
    BookType metadataType();

    /**
     * Search for a metadata source's ID using the provided series identifier.
     * If the series identifier contains a metadata source ID, the ID should be verified
     * from upstream and returned if valid. Otherwise, a search should be performed using the
     * other available information.
     *
     * @return Metadata source's ID. If multiple results are found, return the result that best matches.
     */
    Optional<String> search(SeriesIdentifier seriesIdentifier);

    Optional<Series> fetchSeriesMetadata(String metadataSeriesId);

    default Optional<Release> fetchReleaseMetadata(@Nullable String metadataSeriesId, int index) {
        return Optional.of(
                Release.builder()
                        .title("Release #" + index)
                        .index(index)
                        .build()
        );
    }
}
